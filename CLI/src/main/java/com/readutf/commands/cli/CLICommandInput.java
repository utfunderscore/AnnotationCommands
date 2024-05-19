package com.readutf.commands.cli;

import com.readutf.commands.core.input.CommandInput;

import java.io.OutputStream;

public class CLICommandInput extends CommandInput {
    
    public CLICommandInput(String[] commandParts) {
        super(commandParts);
    }
}
