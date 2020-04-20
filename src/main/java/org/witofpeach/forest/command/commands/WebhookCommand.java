package org.witofpeach.forest.command.commands;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.witofpeach.forest.Config;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;

import java.util.List;

public class WebhookCommand implements ICommand {
    private final String name = "webhook";

    private final WebhookClient client;

    public WebhookCommand() {
        WebhookClientBuilder builder = new WebhookClientBuilder(Config.get("webhook_url"));
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Webhook-Thread");
            thread.setDaemon(true);
            return thread;
        });

        this.client = builder.build();
    }

    @Override
    public void handle(CommandContext commandContext) {
        List<String> args = commandContext.getArgs();
        TextChannel channel = commandContext.getChannel();

        if (args.isEmpty()) {
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        User author = commandContext.getAuthor();

        WebhookMessageBuilder builder = new WebhookMessageBuilder()
                .setUsername(author.getName())
                .setAvatarUrl(author.getEffectiveAvatarUrl().replaceFirst("gif", "png") + "?size=512")
                .setContent(String.join(" ", args));

        client.send(builder.build());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getHelp() {
        return "Senf a webhook message as your name\n" +
                "Usage: `!!webhook [message]`";
    }
}
