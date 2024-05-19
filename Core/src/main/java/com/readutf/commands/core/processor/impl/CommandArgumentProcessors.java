package com.readutf.commands.core.processor.impl;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.input.CommandInput;
import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.adapter.TypeAdapter;
import com.readutf.commands.core.adapter.TypeAdapterManager;
import com.readutf.commands.core.annotation.Param;
import com.readutf.commands.core.exception.CommandProcessingException;
import com.readutf.commands.core.exception.TypeAdapterException;
import com.readutf.commands.core.processor.CommandProcessor;
import com.readutf.commands.core.processor.CommandProcessorInput;

import java.util.List;

public class CommandArgumentProcessors<A extends CommandInput> implements CommandProcessor<A> {

    private final Command command;
    private final TypeAdapterManager<A> typeAdapterManager;

    public CommandArgumentProcessors(Command command, TypeAdapterManager<A> typeAdapterManager) {
        this.command = command;
        this.typeAdapterManager = typeAdapterManager;
    }

    @Override
    public CommandProcessorInput<A> process(CommandProcessorInput<A> data) throws CommandProcessingException {

        List<CommandParameter> commandParams = command.getParametersAnnotatedWith(Param.class);

        System.out.println("available: " + data.getAvailableArgs());

        if(data.getAvailableArgs().size() > commandParams.size()) throw new CommandProcessingException("Too many arguments provided");
        if(data.getAvailableArgs().size() < commandParams.size()) throw new CommandProcessingException("Not enough arguments provided");

        for (int commandParamIndex = 0; commandParamIndex < commandParams.size(); commandParamIndex++) {
            CommandParameter commandParameter = commandParams.get(commandParamIndex);
            TypeAdapter<?> typeAdapter = typeAdapterManager.findTypeAdapter(commandParameter.getType());
            if(typeAdapter == null) throw new CommandProcessingException("No type adapter found for " + commandParameter.getType().getSimpleName());

            try {
                int functionParamIndex = command.getFunctionParameters().indexOf(commandParameter);

                data.getResultParameters()[functionParamIndex] = typeAdapter.adapt(data.getAvailableArgs().get(commandParamIndex), commandParameter);

            } catch (TypeAdapterException e) {
                throw new CommandProcessingException("Failed to adapt type " + commandParameter.getType().getSimpleName(), e);
            }
        }

        return data;
    }
}
