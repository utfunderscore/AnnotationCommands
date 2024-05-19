package com.readutf.commands.core.annotation;

import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SubCommand {


    @Pattern("^[a-zA-Z0-9 ]+$")
    String value();

}
