package command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;

import static KianBot.Main.prefix;

public class RemoveListCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] mess = event.getMessage().getContentRaw().split(" ", 2);

        Cmd cmd = new Cmd("removelist");

        if (mess.length < 2 && cmd.checkMess(mess[0])) {
            EmbedBuilder emb = new EmbedBuilder();
            emb.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
            emb.setColor(Color.decode("#eba22b"));
            emb.addField("Remove Playlist", prefix + "removelist + tênPlaylist + linkNhạc", true);
            event.getChannel().sendMessage(emb.build()).queue();
        }

        if (mess.length == 2 && cmd.checkMess(mess[0])) {
            File file = new File("/home/pi/playlist/"
                    + event.getGuild().getId() + "/" + mess[1] + ".txt");
            if (!file.exists()) {
                event.getChannel().sendMessage("Playlist " + mess[1] + " không tồn tại :exclamation:").queue();
                return;
            }
            file.delete();
            event.getChannel().sendMessage("Đã xóa playlist " + mess[1]).queue();
        }
    }
}
