package command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.awt.Color;

import static KianBot.Main.prefix;

public class HelpCommand extends ListenerAdapter {

    Cmd cmd = new Cmd("help", new String[] {"h", "he"});

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
        emb.setColor(Color.decode("#eba22b"));
        emb.setTitle("Command:");

        String field = "summon: gọi bot\n" +
                "play + link/tênbàihát: phát nhạc\n" +
                "lofi: phát nhạc lofi (hiện không khả dụng)\n" +
                "skip: skip bài hát hiện tại\n" +
                "stop: dừng phát nhạc\n" +
                "pause: tạm dừng nhạc\n" +
                "resume: tiếp tục phát nhạc\n" +
                "queue: xem hàng đợi\n" +
                "nowplaying: xem bài hát hiện tại\n" +
                "loop: lặp lại bài hát hiện tại\n";

        emb.addField("Prefix: " + prefix, field, false);

        if (cmd.checkMess(mess)) {
            event.getChannel().sendMessage(emb.build()).queue();
        }
    }
}
