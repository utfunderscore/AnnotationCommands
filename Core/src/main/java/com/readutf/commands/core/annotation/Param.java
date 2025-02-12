package com.readutf.commands.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.PARAMETER)
public @interface Param {

    /**
     * @return the name of the parameter
     */
    String value();

}
