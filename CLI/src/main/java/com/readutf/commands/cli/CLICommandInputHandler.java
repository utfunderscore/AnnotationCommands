package com.readutf.commands.cli;

import com.readutf.commands.core.input.CommandInputHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class CLICommandInputHandler extends Thread {

    private final CommandInputHandler<CLICommandInput> inputHandler;

    public CLICommandInputHandler(CommandInputHandler<CLICommandInput> inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void run() {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        CLICommandIssuer cliCommandIssuer = new CLICommandIssuer();


        while (!Thread.interrupted()) {
            String input = null;
            try {
                input = bufferedReader.readLine();
                inputHandler.handle(cliCommandIssuer, new CLICommandInput(input.split(" ")));
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

    }
}
