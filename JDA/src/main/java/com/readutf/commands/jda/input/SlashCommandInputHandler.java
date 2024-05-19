package com.readutf.commands.jda.input;

import com.readutf.commands.core.exception.CommandProcessingException;
import com.readutf.commands.core.input.CommandInputHandler;
import com.readutf.commands.jda.impl.JDACommandInput;
import com.readutf.commands.jda.impl.JDACommandIssuer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlashCommandInputHandler extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SlashCommandInputHandler.class);

    private final CommandInputHandler<JDACommandInput> handler;

    public SlashCommandInputHandler(CommandInputHandler<JDACommandInput> handler) {
        this.handler = handler;
    }

    @Override @SubscribeEvent
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        try {
            logger.info("Received slash command: " + event.getCommandString());
            handler.handle(new JDACommandIssuer(event), new JDACommandInput(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
