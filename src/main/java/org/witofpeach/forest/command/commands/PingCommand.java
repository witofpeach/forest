package org.witofpeach.forest.command.commands;

import net.dv8tion.jda.api.JDA;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;

public class PingCommand implements ICommand {
    private String name = "ping";

    @Override
    public void handle(CommandContext commandContext) {
        JDA jda = commandContext.getJDA();

        jda.getRestPing().queue(
                (ping) -> commandContext.getChannel()
                .sendMessageFormat("Rest ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getHelp() {
        return "Shows the current ping from the bot to the discord servers";
    }
}
