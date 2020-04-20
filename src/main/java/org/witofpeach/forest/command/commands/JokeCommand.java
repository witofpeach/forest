package org.witofpeach.forest.command.commands;

import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;

public class JokeCommand implements ICommand {
    private final String name = "joke";

    @Override
    public void handle(CommandContext commandContext) {
        TextChannel channel = commandContext.getChannel();

        WebUtils.ins.getJSONObject("http://dadjokes.online/").async(
                (json) -> {
                    JsonNode joke = json.get("Joke");
                    String opener = joke.get("Opener").asText();
                    String punchline = joke.get("Punchline").asText();
                    EmbedBuilder builder = EmbedUtils.defaultEmbed()
                            .setTitle(opener)
                            .setDescription(punchline);

                    channel.sendMessage(builder.build()).queue();
                }
        );
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getHelp() {
        return "Shows a dad joke\n" +
                "Usage: `!!joke`";
    }
}
