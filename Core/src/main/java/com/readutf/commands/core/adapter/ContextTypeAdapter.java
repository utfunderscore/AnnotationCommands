package com.readutf.commands.core.adapter;

import com.readutf.commands.core.ContextAdapterException;
import com.readutf.commands.core.input.CommandInput;

public interface ContextTypeAdapter<T, U extends CommandInput> {

    T adapt(U input) throws ContextAdapterException;

}
