package com.readutf.commands.core.processor.impl;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.input.CommandInput;
import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.adapter.ContextTypeAdapter;
import com.readutf.commands.core.adapter.TypeAdapterManager;
import com.readutf.commands.core.exception.CommandProcessingException;
import com.readutf.commands.core.processor.CommandProcessor;
import com.readutf.commands.core.processor.CommandProcessorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class CommandContextProcessor<T extends CommandInput> implements CommandProcessor<T> {

    private static final Logger logger = LoggerFactory.getLogger(CommandContextProcessor.class);

    private static final List<Annotation> ALLOWED_ANNOTATIONS = Collections.emptyList();

    private final Command command;
    private final TypeAdapterManager<T> typeAdapterManager;

    public CommandContextProcessor(Command command, TypeAdapterManager<T> typeAdapterManager) {
        this.command = command;
        this.typeAdapterManager = typeAdapterManager;
    }

    @Override
    public CommandProcessorInput<T> process(CommandProcessorInput<T> data) throws CommandProcessingException {

        for (int i = 0; i < command.getFunctionParameters().size(); i++) {
            CommandParameter parameter = command.getFunctionParameters().get(i);
            Class<?> type = parameter.getType();
            ContextTypeAdapter<?, T> typeAdapter = typeAdapterManager.findContextAdapter(type);

            logger.info("Context adapter for {} is {}", type.getSimpleName(), typeAdapter);


            if (typeAdapter == null /*|| hasInvalidAnnotation(parameter)*/) {
                continue;
            }

            try {
                data.getResultParameters()[i] = typeAdapter.adapt(data.getCommandInput());
            } catch (Exception e) {
                throw new CommandProcessingException("Failed to adapt context type " + parameter.getType().getSimpleName(), e);
            }
        }

        return data;
    }

    public boolean hasInvalidAnnotation(CommandParameter parameter) {
        for (Annotation annotation : parameter.getAnnotations()) {
            if (!ALLOWED_ANNOTATIONS.contains(annotation)) {
                return true;
            }
        }
        return false;
    }

}
