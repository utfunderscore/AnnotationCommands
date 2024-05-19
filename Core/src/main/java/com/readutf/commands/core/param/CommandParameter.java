package com.readutf.commands.core.param;

import com.readutf.commands.core.annotation.Param;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.List;

@AllArgsConstructor @Getter
public class CommandParameter {

    private final Class<?> type;
    private final List<Annotation> annotations;

    public boolean isParameter() {
        return annotations.stream().anyMatch(annotation -> annotation instanceof Param);
    }

    public boolean isContext() {
        return !isParameter();
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return annotations.stream().anyMatch(annotation::isInstance);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return (T) annotations.stream().filter(annotation::isInstance).findFirst().orElse(null);
    }


}
