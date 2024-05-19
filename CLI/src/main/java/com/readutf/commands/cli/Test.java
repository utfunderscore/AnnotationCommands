package com.readutf.commands.cli;

import com.readutf.commands.core.annotation.Description;

import java.lang.reflect.Method;

public class Test {

    public static void main(String[] args) {


        System.out.println(new CLICommandManager());

        Method method = null;
        try {
            method = Test.class.getMethod("test");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Description annotation = method.getAnnotation(Description.class);

        System.out.println(annotation);

    }

    @Description("This is a test")
    public void test() {}

}
