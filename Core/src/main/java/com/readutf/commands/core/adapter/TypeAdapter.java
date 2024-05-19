package com.readutf.commands.core.adapter;

import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.exception.TypeAdapterException;

public interface TypeAdapter<T> {

    T adapt(String parameterValue, CommandParameter commandParameter) throws TypeAdapterException;

}

