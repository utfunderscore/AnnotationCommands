package com.readutf.commands.cli;

import com.readutf.commands.core.CommandManager;
import com.readutf.commands.core.annotation.CommandAlias;
import com.readutf.commands.core.annotation.Param;
import com.readutf.commands.core.annotation.SubCommand;

public class CLICommandManager extends CommandManager<CLICommandInput> {

    public CLICommandManager() {

        new CLICommandInputHandler(getCommandExecutor()).start();

        registerCommand(new TestCommand());

    }

    @CommandAlias("test")
    public class TestCommand {

        @SubCommand("test sub")
        public void test(@Param("hello") String hello) {
            System.out.println("Test1 command executed!");
        }

        @SubCommand("test")
        public void test() {
            System.out.println("Test command executed!");
        }

    }

}
