package com.readutf.commands.core.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter @AllArgsConstructor
public abstract class CommandInput  {

    private String[] commandParts;

}
