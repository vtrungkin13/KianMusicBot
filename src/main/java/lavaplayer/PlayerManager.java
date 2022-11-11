package lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeMpegStreamAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new ConcurrentHashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);

    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
           final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

           guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

           return guildMusicManager;
        });
    }

    private String convertTime(long time) {
        int t = (int) time;
        int h = t / 3600;
        t  = t - 3600 * h;
        int m = t / 60;
        int s = t - 60 * m;

        String hour, min, sec;
        if (h < 10)
            hour = "0" + String.valueOf(h);
        else
            hour = String.valueOf(h);

        if (m < 10)
            min = "0" + String.valueOf(m);
        else
            min = String.valueOf(m);

        if (s < 10)
            sec = "0" + String.valueOf(s);
        else
            sec = String.valueOf(s);

        String length = " (" + hour + ':' + min + ':' + sec + ").";

        return length;
    }

    public void loadAndPlay(TextChannel channel, final String trackUrl) {
        final GuildMusicManager musicManager  = getMusicManager(channel.getGuild());

        audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                if (trackUrl.equals("https://youtu.be/5qap5aO4i9A")) {
                    channel.sendMessage(":musical_note: Đang phát nhạc lofi").queue();
                }
                else {
                    channel.sendMessage(":notes: Thêm vào hàng đợi: ")
                        .append(track.getInfo().title)
                        .append(" by ")
                        .append(track.getInfo().author)
                        .append(convertTime(track.getInfo().length/1000))
                        .queue();
                }

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                if (trackUrl.substring(0, 8).equals("ytsearch")) {
                    musicManager.scheduler.queue(tracks.get(0));

                    channel.sendMessage(":notes: Thêm vào hàng đợi: ")
                            .append(tracks.get(0).getInfo().title)
                            .append(" by ")
                            .append(tracks.get(0).getInfo().author)
                            .append(convertTime(tracks.get(0).getInfo().length/1000))
                            .queue();
                }

                else {

                    for (AudioTrack item : tracks) {
                        musicManager.scheduler.queue(item);
                    }

                    channel.sendMessage(":notes: Thêm vào hàng đợi: ")
                            .append(String.valueOf(tracks.size()))
                            .append(" bài hát từ ")
                            .append(playlist.getName())
                            .queue();

                }

            }

            @Override
            public void noMatches() {
                channel.sendMessage("Không tìm thấy nhạc :o:").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Không load được nhạc :x:").queue();
            }

        });
    }


    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }


}
