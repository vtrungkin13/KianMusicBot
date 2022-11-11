package command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import static KianBot.Main.prefix;

public class QueueCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        Cmd cmd = new Cmd("queue");

        if (cmd.checkMess(mess)) {

            final TextChannel channel = event.getChannel();
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
            if (queue.isEmpty()) {
                channel.sendMessage("Hàng đợi trống :o:").queue();
                return;
            }

            final int trackCount = Math.min(queue.size(), 20);
            final List<AudioTrack> trackList = new ArrayList<>(queue);
            final MessageAction messageAction = channel.sendMessage(":infinity: Hàng đợi:\n");

            for (int i=0; i<trackCount; i++) {
                final AudioTrack track = trackList.get(i);
                final AudioTrackInfo info = track.getInfo();

                messageAction.append('#')
                        .append(String.valueOf(i + 1))
                        .append(" ")
                        .append(info.title)
                        .append("__")
                        .append(info.author + "\n");
            }

            if (trackList.size() > trackCount) {
                messageAction.append(String.valueOf(trackList.size() - trackCount))
                        .append(" bài khác...");
            }

            messageAction.queue();
        }

    }
}
