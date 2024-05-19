package com.readutf.commands.core.processor;

import com.readutf.commands.core.exception.CommandProcessingException;
import com.readutf.commands.core.input.CommandInput;

/**
 * Executes when a command executes
 */
public interface CommandProcessor<T extends CommandInput> {

    /**
     * Process the command args as neccessary and return the modified
     * args and data
     * <p>
     * If an argument should not be visible to the next processor
     * it should be removed at this stage
     */
    CommandProcessorInput<T> process(CommandProcessorInput<T> data) throws CommandProcessingException;

}

