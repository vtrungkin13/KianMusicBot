package command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URISyntaxException;

import static KianBot.Main.prefix;

public class AppendListCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] mess = event.getMessage().getContentRaw().split(" ", 3);

        Cmd cmd = new Cmd("appendlist");

        if (mess.length < 3 && cmd.checkMess(mess[0])) {
            EmbedBuilder emb = new EmbedBuilder();
            emb.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
            emb.setColor(Color.decode("#eba22b"));
            emb.addField("Append PlayList", prefix + "appendlist + tênPlaylist + linkNhạc", true);
            event.getChannel().sendMessage(emb.build()).queue();
        }

        if (mess.length == 3 && cmd.checkMess(mess[0])) {
            try {
                String folderPath = "/home/pi/KianBot/playlist/" + event.getGuild().getId();

                File severFolder = new File(folderPath);

                if (!severFolder.exists()) {
                    severFolder.mkdir();
                }

                File file = new File("/home/pi/playlist/"
                        + event.getGuild().getId() + "/" + mess[1] + ".txt");
                if (!file.exists()) {
                    event.getChannel().sendMessage("Playlist " + mess[1] + " không tồn tại :exclamation:").queue();
                    return;
                }

                if (file.exists()) {
                    file.createNewFile();
                    if (!isUrl(mess[2])) {
                        event.getChannel().sendMessage("Link không hợp lệ :no_entry_sign:").queue();
                    } else {
                        FileWriter fileWriter = new FileWriter(file, true);
                        BufferedWriter writer = new BufferedWriter(fileWriter);
                        writer.write("\n" + mess[2]);
                        event.getChannel().sendMessage(":white_check_mark: Đã thêm vào playlist " + mess[1] + ": " + mess[2]).queue();
                        writer.close();
                    }
                }

            } catch (Exception e) {

            }
        }
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch(URISyntaxException e) {
            return false;
        }
    }
}
