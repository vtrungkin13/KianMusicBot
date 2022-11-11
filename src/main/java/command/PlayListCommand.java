package command;

import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.io.*;

import static KianBot.Main.prefix;

public class PlayListCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] mess = event.getMessage().getContentRaw().split(" ", 2);

        final TextChannel channel = event.getChannel();

        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoice = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memVoice = member.getVoiceState();

        Cmd plCmd = new Cmd("playlist", new String[] {"pl"});

        if (mess.length == 1 && plCmd.checkMess(mess[0])) {
            EmbedBuilder emb = new EmbedBuilder();
            emb.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
            emb.setColor(Color.decode("#eba22b"));
            emb.setTitle("Playlist Command:");
            String field = "playlist/pl + tênPlaylist: phát một playlist\n" +
                    "addplaylist/apl + tênPlaylist + linkNhạc: tạo một playlist tùy chỉnh\n" +
                    "appendlist + tênPlaylist + linkNhạc: thêm link nhạc vào một playlist\n" +
                    "showlist: các playlist hiện có\n" +
                    "removelist + tênPLaylist: xóa một playlist\n";
            emb.addField("Prefix: " + prefix, field, false);
            event.getChannel().sendMessage(emb.build()).queue();
        }

        int count = 0;

        File file = new File("/home/pi/playlist/" + event.getGuild().getId());
        File[] listOfFile = file.listFiles();
        for (File item: listOfFile) {
            if (plCmd.checkMess(mess[0]) && mess[1].equalsIgnoreCase(item.getName().substring(0, item.getName().length() - 4))) {
                if (!selfVoice.inVoiceChannel()) {
                    final AudioManager audioManager = event.getGuild().getAudioManager();
                    final VoiceChannel memChannel = memVoice.getChannel();
                    audioManager.openAudioConnection(memChannel);
                }

                count = 1;
                try {
                    File playlist = new File("/home/pi/playlist/"
                            + event.getGuild().getId() + "/" + item.getName());
                    FileReader fileReader = new FileReader(playlist);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        PlayerManager.getInstance().loadAndPlay(channel, line);
                    }
                } catch (Exception e) {

                }
                if (count == 0) {
                    event.getChannel().sendMessage("Không tìm thấy playlist :no_entry_sign:").queue();
                }
            }

        }

        Cmd slCmd = new Cmd("showlist");

        if (mess.length == 1 && slCmd.checkMess(mess[0])) {
            String fileName = "";
            for (File item: listOfFile)
                fileName += item.getName().substring(0, item.getName().length() - 4) + "\n";
            EmbedBuilder emb = new EmbedBuilder();
            emb.setAuthor(event.getJDA().getSelfUser().getName(), null, event.getJDA().getSelfUser().getAvatarUrl());
            emb.setColor(Color.decode("#eba22b"));
            emb.setTitle(prefix + "playlist/" + prefix + "pl + Tênplaylist");
            emb.addField("Playlist hiện có:", fileName, false);
            event.getChannel().sendMessage(emb.build()).queue();
        }
    }
}
