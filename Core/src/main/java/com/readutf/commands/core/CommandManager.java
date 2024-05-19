package com.readutf.commands.core;

import com.readutf.commands.core.adapter.ContextTypeAdapter;
import com.readutf.commands.core.adapter.TypeAdapter;
import com.readutf.commands.core.adapter.TypeAdapterManager;
import com.readutf.commands.core.input.CommandInput;
import com.readutf.commands.core.input.CommandInputHandler;
import com.readutf.commands.core.registry.CommandRegistry;
import lombok.Getter;

@Getter
public abstract class CommandManager<A extends CommandInput> {

    protected final TypeAdapterManager<A> typeAdapterManager;
    protected final CommandRegistry commandRegistry;
    protected final CommandInputHandler<A> commandExecutor;

    public CommandManager() {
        this.typeAdapterManager = new TypeAdapterManager<>();
        this.commandRegistry = new CommandRegistry(typeAdapterManager);
        this.commandExecutor = new CommandExecutor<>(commandRegistry, typeAdapterManager);
    }

    public void registerCommand(Object object) {
        commandRegistry.scanAndRegister(object);
    }

    public <T> void registerTypeAdapter(Class<T> type, TypeAdapter<T> adapter) {
        typeAdapterManager.registerAdapter(type, adapter);
    }

    public <T> void registerContextAdapter(Class<T> type, ContextTypeAdapter<T, A> adapter) {
        typeAdapterManager.registerContextAdapter(type, adapter);
    }

}
