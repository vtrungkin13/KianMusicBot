package command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LoopCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        Cmd cmd = new Cmd("loop");

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        final AudioTrackInfo info = track.getInfo();

        if (cmd.checkMess(mess)) {
            musicManager.scheduler.setRepeating(!musicManager.scheduler.isRepeating());
            if (musicManager.scheduler.isRepeating())
                event.getChannel().sendMessage("Đang lặp lại " + info.title).queue();
            else
                event.getChannel().sendMessage("Hủy lặp lại " + info.title).queue();
        }
    }
}
