package org.witofpeach.forest.command.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;

import java.security.PrivilegedExceptionAction;
import java.util.List;

public class KickCommand implements ICommand {
    private String name = "kick";

    @Override
    public void handle(CommandContext commandContext) {
        TextChannel channel = commandContext.getChannel();

        Message message = commandContext.getMessage();

        List<String> args = commandContext.getArgs();


        String reason = String.join(" ", args.subList(1, args.size()));

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        Member target = message.getMentionedMembers().get(0);
        Member member = commandContext.getMember();

        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("You are missing permission to kick this member").queue();
            return;
        }

        Member selfMember = commandContext.getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
            channel.sendMessage("I am missing permissions to kick this member").queue();
            return;
        }

        commandContext.getGuild()
                .kick(target, reason)
                .reason(reason)
                .queue(
                        (__) -> channel.sendMessage("Kick was successful").queue(),
                        (error) -> channel.sendMessageFormat("Could not kick %s", error.getMessage()).queue()
                );
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getHelp() {
        return "Kick a member off the server.\n" +
                "Usage: `!!kick <@user> <reason>`";
    }
}
