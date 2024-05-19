package com.readutf.commands.core;

import com.readutf.commands.core.adapter.TypeAdapterManager;
import com.readutf.commands.core.exception.CommandProcessingException;
import com.readutf.commands.core.input.CommandInput;
import com.readutf.commands.core.input.CommandInputHandler;
import com.readutf.commands.core.issuer.CommandIssuer;
import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.processor.CommandProcessor;
import com.readutf.commands.core.processor.CommandProcessorFactory;
import com.readutf.commands.core.processor.CommandProcessorInput;
import com.readutf.commands.core.processor.impl.CommandArgumentProcessors;
import com.readutf.commands.core.processor.impl.CommandContextProcessor;
import com.readutf.commands.core.registry.CommandRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Parameter;
import java.util.*;

public class CommandExecutor<A extends CommandInput> implements CommandInputHandler<A> {

    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    private final CommandRegistry commandRegistry;
    private final List<CommandProcessorFactory<A>> commandProcessorFactories;

    public CommandExecutor(CommandRegistry commandRegistry, TypeAdapterManager<A> typeAdapterManager) {
        this.commandRegistry = commandRegistry;
        this.commandProcessorFactories = new ArrayList<>();

        commandProcessorFactories.add(command -> new CommandContextProcessor<>(command, typeAdapterManager));
        commandProcessorFactories.add(command -> new CommandArgumentProcessors<>(command, typeAdapterManager));
    }

    public void handle(CommandIssuer commandIssuer, A input) throws CommandProcessingException {

        String[] commandParts = input.getCommandParts();

        String fullCommand = String.join(" ", commandParts);

        //Scan command map for potential commands
        List<Command> potentialCommands = commandRegistry.getCommands(input);

        if (potentialCommands.size() > 1) {
            logger.debug("Found multiple potential commands for input: " + fullCommand);

            /*
              Example:
              /command sub hello
              /command sub <input>
              <p>
              Match users input to the longest number of matching arguments
             */
            List<Command> sortedCommands = potentialCommands.stream().sorted(Comparator.comparingInt(value -> value.getPathParts().length)).toList();
            for (Command sortedCommand : sortedCommands) {
                if (sortedCommand.getPath().equalsIgnoreCase(fullCommand.substring(0, Math.min(fullCommand.length(), sortedCommand.getPath().length())))) {
                    potentialCommands = new ArrayList<>();
                    potentialCommands.add(sortedCommand);
                    break;
                } else {
                    potentialCommands.remove(sortedCommand);
                }
            }


            if (potentialCommands.size() > 1) {
                commandIssuer.sendError("Multiple commands found, please be more specific");
                return;
            }

        }

        if (potentialCommands.isEmpty()) {
            commandIssuer.sendError("Command not found");
            return;
        }

        Command foundCommand = potentialCommands.get(0);

        String[] commandPathParts = foundCommand.getPath().split(" ");
        if (commandPathParts.length > commandParts.length) {
            commandIssuer.sendError("Invalid usage, try: " + foundCommand.getPath());
            return;
        }

        String[] argsWithoutPath = new String[commandParts.length - commandPathParts.length];
        System.arraycopy(commandParts, commandPathParts.length, argsWithoutPath, 0, argsWithoutPath.length);

        CommandProcessorInput<A> commandProcessorInput = new CommandProcessorInput<>(input, new ArrayList<>(Arrays.asList(argsWithoutPath)), new Object[foundCommand.getFunctionParameters().size()]);
        for (CommandProcessorFactory<A> commandProcessorFactory : commandProcessorFactories) {
            CommandProcessor<A> processor = commandProcessorFactory.createProcessor(foundCommand);


            try {
                commandProcessorInput = processor.process(commandProcessorInput);
            } catch (CommandProcessingException e) {
                commandIssuer.sendMessage("Failed to process command");
                throw e;
            }
        }

        Object[] parameters = commandProcessorInput.getResultParameters();
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            if(parameter == null) logger.error("Failed to resolve parameter: " + (i + 1));
        }

        System.out.println(Arrays.toString(parameters));


        try {
            foundCommand.execute(parameters);
        } catch (Exception e) {
            e.printStackTrace();
            commandIssuer.sendError("Failed to execute command");
        }
    }

}
