package com.readutf.commands.core.issuer;

public interface CommandIssuer {

    void sendMessage(String message);

    void sendError(String error);

}
