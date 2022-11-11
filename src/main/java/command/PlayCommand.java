package command;

import java.net.URI;
import java.net.URISyntaxException;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import static KianBot.Main.prefix;

public class PlayCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] mess = event.getMessage().getContentRaw().split(" ", 2);

        final TextChannel channel = event.getChannel();

        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoice = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memVoice = member.getVoiceState();

        Cmd cmd = new Cmd("play", new String[] {"p"});

        if (mess.length == 1 && cmd.checkMess(mess[0])) {

            if (!memVoice.inVoiceChannel()) {
                channel.sendMessage("Bạn chưa vào kênh thoại").queue();
                return;
            }


            if (!selfVoice.inVoiceChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memChannel = memVoice.getChannel();
                audioManager.openAudioConnection(memChannel);

            }


            channel.sendMessage("Chọn nhạc để phát [play + link/tênbàihát]").queue();

        }

        else if (mess.length > 1 && cmd.checkMess(mess[0])) {
            if (!selfVoice.inVoiceChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memChannel = memVoice.getChannel();
                audioManager.openAudioConnection(memChannel);
            }


            String link = mess[1];

            if (!isUrl(link)) {
                link = "ytsearch:" + link;
            }
            if (!memVoice.inVoiceChannel()) {
                channel.sendMessage("Bạn chưa vào kênh thoại").queue();
                return;
            }


            PlayerManager.getInstance().loadAndPlay(channel, link);
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
