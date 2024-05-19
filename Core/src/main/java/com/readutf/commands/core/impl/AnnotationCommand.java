package com.readutf.commands.core.impl;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.annotation.Description;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotationCommand extends Command {

    private final Method method;
    private final Object instance;

    public AnnotationCommand(String commandPath, @NotNull Method method, Object instance) {
        super(commandPath, getCommandParameters(method));
        this.method = method;
        this.instance = instance;
    }

    @Override
    public void execute(Object[] input) {
        try {
            method.invoke(instance, input);
        } catch (IllegalAccessException | InvocationTargetException e) {
            //TODO: Better error handling
            e.printStackTrace();
        }
    }

    @Override
    public String getDescription() {
        Description annotation = method.getAnnotation(Description.class);
        return annotation == null ? "" : annotation.value();
    }

    private static List<CommandParameter> getCommandParameters(Method method) {
        ArrayList<CommandParameter> parameters = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            parameters.add(new CommandParameter(parameter.getType(), Arrays.asList(parameter.getAnnotations())));
        }

        return parameters;

    }

    @Override
    public String toString() {
        return "AnnotationCommand{" +
                "method=" + method +
                ", instance=" + instance +
                "} " + super.toString();
    }
}
