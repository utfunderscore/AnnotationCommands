package com.readutf.commands.core.registry;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.adapter.TypeAdapterManager;
import com.readutf.commands.core.annotation.CommandAlias;
import com.readutf.commands.core.annotation.DefaultCommand;
import com.readutf.commands.core.annotation.SubCommand;
import com.readutf.commands.core.impl.AnnotationCommand;
import com.readutf.commands.core.input.CommandInput;
import com.readutf.commands.core.utils.CommandMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandRegistry {

    private final CommandMap commandMap;

    private final TypeAdapterManager typeAdapterManager;

    public CommandRegistry(TypeAdapterManager typeAdapterManager) {
        this.typeAdapterManager = typeAdapterManager;
        this.commandMap = new CommandMap();
    }

    public List<Command> scanAndRegister(Object object) {
        Class<?> aClass = object.getClass();

        String topLevelCommand = aClass.isAnnotationPresent(CommandAlias.class) ? aClass.getAnnotation(CommandAlias.class).value() : "";

        System.out.println("topLevel: " + topLevelCommand);
        ArrayList<Command> commands = new ArrayList<>();

        for (Method method : aClass.getMethods()) {

            if (method.isAnnotationPresent(CommandAlias.class)) {
                String commandName = method.getAnnotation(CommandAlias.class).value();

                Command command = new AnnotationCommand(commandName, method, object);
                commandMap.addCommand(commandName, command);
                commands.add(command);
            } else if (method.isAnnotationPresent(SubCommand.class)) {

                String commandName = method.getAnnotation(SubCommand.class).value();

                System.out.println("sub: " + commandName);

                Command command = new AnnotationCommand(topLevelCommand + " " + commandName, method, object);
                commandMap.addCommand(topLevelCommand + " " + commandName, command);

                commands.add(command);
            } else if(method.isAnnotationPresent(DefaultCommand.class)) {

                if(topLevelCommand.equalsIgnoreCase("")) {
                    System.err.println("Default command requires the class to be annotated with @CommandAlias");
                    continue;
                }

                Command command = new AnnotationCommand(topLevelCommand, method, object);
                commandMap.addCommand(topLevelCommand, command);
                commands.add(command);
            }

        }
        return commands;
    }

    public @NotNull List<Command> getCommands(CommandInput commandInput) {
        return commandMap.getCommand(commandInput);
    }
}
