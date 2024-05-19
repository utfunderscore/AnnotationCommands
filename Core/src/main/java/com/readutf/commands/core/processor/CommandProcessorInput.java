package com.readutf.commands.core.processor;

import com.readutf.commands.core.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class CommandProcessorInput<T extends CommandInput> {

    private T commandInput;
    private List<String> availableArgs;
    private Object[] resultParameters;

    public CommandProcessorInput(T commandInput, List<String> availableArgs, Object[] resultParameters) {
        this.commandInput = commandInput;
        this.availableArgs = availableArgs;
        this.resultParameters = resultParameters;
    }
}
