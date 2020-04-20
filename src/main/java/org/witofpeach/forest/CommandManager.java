package org.witofpeach.forest;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;
import org.witofpeach.forest.command.commands.HelpCommand;
import org.witofpeach.forest.command.commands.KickCommand;
import org.witofpeach.forest.command.commands.MemeCommand;
import org.witofpeach.forest.command.commands.PingCommand;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new KickCommand());
        addCommand(new MemeCommand());
    }

    private void addCommand(ICommand command) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(command.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present.");
        }

        commands.add(command);
    }

    public List<ICommand> getAllCommands() {
        return this.commands;
    }

    @Nullable
    public ICommand getCommand(String name) {
        String nameLower = name.toLowerCase();

        for (ICommand command : this.commands) {
            if (command.getName().equals(nameLower) || command.getAliases().contains(nameLower)) {
                return command;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand command = this.getCommand(invoke);

        event.getChannel().sendTyping().queue();

        List<String> args = Arrays.asList(split).subList(1, split.length);

        CommandContext commandContext = new CommandContext(event, args);

        if (command != null) {
            command.handle(commandContext);
        } else {
            commandContext.getChannel()
                    .sendMessageFormat("Command !!%s not found!", invoke).queue();
        }
    }

}


