package command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static KianBot.Main.prefix;

public class SkipCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        final TextChannel channel = event.getChannel();

        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoice = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memVoice = member.getVoiceState();

        Cmd cmd = new Cmd("skip");

        if (cmd.checkMess(mess)) {
            if (!memVoice.inVoiceChannel()) {
                channel.sendMessage("Bạn chưa vào kênh thoại").queue();
                return;
            }

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if (audioPlayer.getPlayingTrack() == null) {
                channel.sendMessage("Không có bài hát đang phát :interrobang:").queue();
                return;
            }

            channel.sendMessage(":track_next: Skipped " + audioPlayer.getPlayingTrack().getInfo().title).queue();
            musicManager.scheduler.setRepeating(false);
            musicManager.scheduler.nextTrack();

        }
    }
}
