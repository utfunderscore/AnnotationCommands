package com.readutf.commands.core;

import com.readutf.commands.core.param.CommandParameter;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;

@Getter
public abstract class Command {

    private final String path;
    private final List<CommandParameter> functionParameters;

    public Command(String path, List<CommandParameter> functionParameters) {
        this.path = path;
        this.functionParameters = functionParameters;
    }

    public abstract void execute(Object[] input);

    public @NotNull
    abstract String getDescription();

    public String[] getPathParts() {
        return path.split(" ");
    }

    public List<CommandParameter> getParametersAnnotatedWith(Class<? extends Annotation> clazz) {
        return functionParameters.stream().filter(commandParameter ->  commandParameter.hasAnnotation(clazz)).toList();
    }

    @Override
    public String toString() {
        return "Command{" +
                "path='" + path + '\'' +
                ", functionParameters=" + functionParameters +
                '}';
    }
}
