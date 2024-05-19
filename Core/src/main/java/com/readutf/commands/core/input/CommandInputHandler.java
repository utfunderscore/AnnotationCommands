package com.readutf.commands.core.input;

import com.readutf.commands.core.exception.CommandProcessingException;
import com.readutf.commands.core.issuer.CommandIssuer;

public interface CommandInputHandler<A extends CommandInput> {

    void handle(CommandIssuer commandIssuer, A input) throws CommandProcessingException;

}
