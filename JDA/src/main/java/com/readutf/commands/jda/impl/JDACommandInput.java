package com.readutf.commands.jda.impl;

import com.readutf.commands.core.input.CommandInput;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@Getter
public class JDACommandInput extends CommandInput {

    private final SlashCommandInteractionEvent event;

    public JDACommandInput(SlashCommandInteractionEvent event) {
        super(event.getCommandString().substring(1).split(" "));
        this.event = event;
    }

}
