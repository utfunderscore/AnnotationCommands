package com.readutf.commands.jda.impl;

import com.readutf.commands.core.issuer.CommandIssuer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class JDACommandIssuer implements CommandIssuer {

    private final SlashCommandInteractionEvent event;

    public JDACommandIssuer(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    @Override
    public void sendMessage(String message) {
        event.replyEmbeds(new EmbedBuilder().setDescription(message).build()).queue();
    }

    @Override
    public void sendError(String error) {
        event.replyEmbeds(new EmbedBuilder().setColor(Color.GREEN).setTitle("Error").setDescription(error).build()).queue();
    }
}
