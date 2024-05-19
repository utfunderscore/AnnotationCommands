package com.readutf.commands.core.adapter.primitive;

import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.adapter.TypeAdapter;
import com.readutf.commands.core.exception.TypeAdapterException;

import java.text.NumberFormat;
import java.text.ParseException;

public class NumberParser implements TypeAdapter<Number> {

    @Override
    public Number adapt(String parameterValue, CommandParameter commandParameter) throws TypeAdapterException {

        try {
            return NumberFormat.getInstance().parse(parameterValue);
        } catch (ParseException e) {
            throw new TypeAdapterException("Failed to convert " + parameterValue + " to a number", e);
        }

    }
}
