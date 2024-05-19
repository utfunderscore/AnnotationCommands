package com.readutf.commands.core.processor;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.input.CommandInput;

public interface CommandProcessorFactory<A extends CommandInput> {

    CommandProcessor<A> createProcessor(Command command);

}
