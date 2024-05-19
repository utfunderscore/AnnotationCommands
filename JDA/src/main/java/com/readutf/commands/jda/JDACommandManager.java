package com.readutf.commands.jda;

import com.readutf.commands.core.Command;
import com.readutf.commands.core.CommandManager;
import com.readutf.commands.core.ContextAdapterException;
import com.readutf.commands.core.annotation.Param;
import com.readutf.commands.core.param.CommandParameter;
import com.readutf.commands.core.utils.CommandMap;
import com.readutf.commands.jda.impl.JDACommandInput;
import com.readutf.commands.jda.input.SlashCommandInputHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JDACommandManager extends CommandManager<JDACommandInput> {

    private static Logger logger = LoggerFactory.getLogger(JDACommandManager.class);

    private static Map<Class<?>, OptionType> optionTypeMap;

    private final JDA jda;

    public JDACommandManager(JDA jda) {
        this.jda = jda;

        optionTypeMap = Map.of(
                String.class, OptionType.STRING,
                Integer.class, OptionType.INTEGER,
                Boolean.class, OptionType.BOOLEAN
        );

        jda.addEventListener(new SlashCommandInputHandler(getCommandExecutor()));

        registerContextAdapter(JDA.class, input -> jda);
        registerContextAdapter(Member.class, input -> input.getEvent().getMember());
        registerContextAdapter(User.class, input -> input.getEvent().getUser());
        registerContextAdapter(TextChannel.class, input -> {
            MessageChannelUnion channel = input.getEvent().getChannel();
            if(channel.getType() != ChannelType.TEXT) throw new ContextAdapterException("This command can only be used in a text channel.");
            return channel.asTextChannel();
        });
        registerContextAdapter(PrivateChannel.class, input -> {
            MessageChannelUnion channel = input.getEvent().getChannel();
            if(channel.getType() != ChannelType.PRIVATE) throw new ContextAdapterException("This command can only be used in a text channel.");
            return channel.asPrivateChannel();
        });
        registerContextAdapter(NewsChannel.class, input -> {
            MessageChannelUnion channel = input.getEvent().getChannel();
            if(channel.getType() != ChannelType.GROUP) throw new ContextAdapterException("This command can only be used in a text channel.");
            return channel.asNewsChannel();
        });
        registerContextAdapter(MessageChannelUnion.class, input -> input.getEvent().getChannel());
        registerContextAdapter(SlashCommandInteractionEvent.class, JDACommandInput::getEvent);
    }

    public void buildCommands() {

        CommandMap commandMap = getCommandRegistry().getCommandMap();

        List<SlashCommandData> commands = new ArrayList<>();

        CommandMap.Node tree = commandMap.getTree();
        Collection<CommandMap.Node> commandNodes = tree.getChildren().values();

        for (CommandMap.Node commandNode : commandNodes) {
            if(commandNode.getDepth() > 1) {
                logger.warn("Error registering {}, Discord Commands can only have one layer of sub commands", commandNode.getCommandPart());
                continue;
            }

            if(commandNode.getDepth() == 0) {

                Command command = commandNode.getValue();
                if(command == null) continue;

                SlashCommandData slashCommandData = convertToDiscordCommand(command);
                commands.add(slashCommandData);
                logger.info("Registered command: {} {}", command.getPath(), slashCommandData);
            } else {

                List<SubcommandData> subCommands = commandNode.getChildren().values().stream()
                        .filter(node -> node.getValue() != null) //Practically impossible but just encase
                        .map(node -> convertToDiscordSubCommand(node.getValue()))
                        .toList();

                SlashCommandData slashCommandData = Commands.slash(commandNode.getCommandPart(), "test");
                slashCommandData.addSubcommands(subCommands);

                commands.add(slashCommandData);
                logger.info("Registered sub command: {}", commandNode.getCommandPart());

                if(commandNode.getValue() != null) {
                    logger.warn("Error registering {}, Discord Commands do not support default commands.", commandNode.getCommandPart());
                }
            }

//            List<net.dv8tion.jda.api.interactions.commands.Command> jdaCommands = jda.retrieveCommands().complete();
//
//            for (net.dv8tion.jda.api.interactions.commands.Command jdaCommand : jdaCommands) {
//                jdaCommand.delete().complete();
//                logger.info("Deleted command: {}", jdaCommand.getName());
//            }

            jda.updateCommands().addCommands(commands).queue();
        }
    }

    @NotNull
    private SlashCommandData convertToDiscordCommand(Command value) {
        List<CommandParameter> parameters = value.getParametersAnnotatedWith(Param.class);

        SlashCommandData slashCommandData = Commands.slash(value.getPath(), "test");

        for (CommandParameter parameter : parameters) {
            Param param = parameter.getAnnotation(Param.class);

            slashCommandData.addOption(getOptionType(parameter.getType()), param.value(), "", true, false);
        }
        return slashCommandData;
    }

    @NotNull
    private SubcommandData convertToDiscordSubCommand(Command value) {
        List<CommandParameter> parameters = value.getParametersAnnotatedWith(Param.class);
        String[] pathParts = value.getPathParts();

        SubcommandData subcommandData = new SubcommandData(pathParts[pathParts.length - 1], "test");
        for (CommandParameter parameter : parameters) {
            Param param = parameter.getAnnotation(Param.class);

            subcommandData.addOption(getOptionType(parameter.getType()), param.value(), "test", true, false);
        }

        return subcommandData;
    }

    public OptionType getOptionType(Class<?> type) {
        return optionTypeMap.getOrDefault(type, OptionType.STRING);
    }

}
