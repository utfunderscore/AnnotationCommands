package com.readutf.commands.core.exception;

public class CommandProcessingException extends Exception {

    public CommandProcessingException(String message) {
        super(message);
    }

    public CommandProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
