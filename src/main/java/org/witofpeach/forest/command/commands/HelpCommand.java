package org.witofpeach.forest.command.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import org.witofpeach.forest.CommandManager;
import org.witofpeach.forest.Config;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;

import java.util.List;

public class HelpCommand implements ICommand {
    private final CommandManager manager;
    private String name = "help";

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist", "h");
    }

    @Override
    public void handle(CommandContext commandContext) {
        List<String> args = commandContext.getArgs();
        TextChannel channel = commandContext.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            builder.append("List of commands\n");

            manager.getAllCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append('`').append(Config.get("prefix")).append(it).append("`\n")
            );

            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String name = args.get(0);
        ICommand command = manager.getCommand(name);

        if (command == null) {
            channel.sendMessage("Nothing found for " + name).queue();
        } else {
            channel.sendMessage(command.getHelp()).queue();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getHelp() {
        return "Shows the list of commands in the bot\n" +
                "Usage: `!!help [command]`";
    }
}
