package com.readutf.commands.cli;

import com.readutf.commands.core.issuer.CommandIssuer;

public class CLICommandIssuer implements CommandIssuer {

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void sendError(String error) {
        System.err.println(error);
    }

}
