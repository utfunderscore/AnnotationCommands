package com.readutf.commands.core.adapter;

import com.readutf.commands.core.adapter.primitive.EnumParser;
import com.readutf.commands.core.adapter.primitive.NumberParser;
import com.readutf.commands.core.adapter.primitive.StringParser;
import com.readutf.commands.core.input.CommandInput;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TypeAdapterManager<T extends CommandInput> {

    private static final Logger logger = LoggerFactory.getLogger(TypeAdapterManager.class);

    private final Map<Class<?>, TypeAdapter<?>> parameterAdapters;
    private final Map<Class<?>, ContextTypeAdapter<?, T>> contextAdapters;

    public TypeAdapterManager() {
        this.parameterAdapters = new HashMap<>();
        this.contextAdapters = new HashMap<>();

        registerAdapter(String.class, new StringParser());
        registerAdapter(Number.class, new NumberParser());
        registerAdapter(Enum.class, new EnumParser());
    }

    public <A> void registerAdapter(Class<A> type, TypeAdapter<A> adapter) {
        parameterAdapters.put(type, adapter);
    }

    public <A> void registerContextAdapter(Class<A> type, ContextTypeAdapter<A, T> adapter) {
        contextAdapters.put(type, adapter);
    }

    public @Nullable TypeAdapter<?> findTypeAdapter(Class<?> type) {
        TypeAdapter<?> exactType = parameterAdapters.get(type);
        if (exactType != null) return exactType;

        for (Class<?> aClass : parameterAdapters.keySet()) {
            if (aClass.isAssignableFrom(type)) {
                return parameterAdapters.get(aClass);
            }
        }

        return null;
    }

    public @Nullable ContextTypeAdapter<?, T> findContextAdapter(Class<?> type) {
        ContextTypeAdapter<?, T> exactType = contextAdapters.get(type);
        if (exactType != null) return exactType;

        for (Class<?> aClass : contextAdapters.keySet()) {

            logger.debug("Checking if {} is assignable from {}", type.getSimpleName(), aClass.getSimpleName());



            if (type.isAssignableFrom(aClass)) {
                return contextAdapters.get(aClass);
            }
        }

        return null;
    }

}
