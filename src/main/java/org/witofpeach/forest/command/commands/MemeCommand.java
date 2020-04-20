package org.witofpeach.forest.command.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.entities.TextChannel;
import org.witofpeach.forest.command.CommandContext;
import org.witofpeach.forest.command.ICommand;

import java.util.ArrayList;
import java.util.List;

public class MemeCommand implements ICommand {
    private final String name = "meme";

    @Override
    public void handle(CommandContext commandContext) {
        List<String> args = commandContext.getArgs();
        TextChannel channel = commandContext.getChannel();
        if (args.size() > 1) {
            channel.sendMessage("Too many arguments for the meme query");
            return;
        }
        if (args.isEmpty()) {
            args = new ArrayList<>();
            args.add("1");
        }
        WebUtils.ins.getJSONObject("https://meme-api.herokuapp.com/gimme/" + args.get(0)).async((json) -> {
            ArrayNode memes = json.withArray("memes");
            memes.forEach(
                    (meme) -> channel.sendMessage(EmbedUtils.embedImageWithTitle(meme.get("title").asText(),
                            meme.get("postLink").asText(), meme.get("url").asText()).build()).queue()
            );


//            String title = meme.get("title").asText();
//            String postlink = meme.get("postlink").asText();
//            String url = meme.get("url").asText();
//            EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, postlink, url);
//            channel.sendMessage(embed.build()).queue();

        });
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getHelp() {
        return "Shows funny memes\n" +
                "Usage: `!!meme <amount>`";
    }
}
