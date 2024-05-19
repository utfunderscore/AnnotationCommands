package com.readutf.commands.core.adapter.primitive;

import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.adapter.TypeAdapter;

public class StringParser implements TypeAdapter<String> {

    @Override
    public String adapt(String parameterValue, CommandParameter commandParameter) {
        return parameterValue;
    }
}
