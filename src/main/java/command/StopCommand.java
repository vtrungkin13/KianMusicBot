package command;


import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class StopCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        final TextChannel channel = event.getChannel();

        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoice = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memVoice = member.getVoiceState();

        Cmd cmd = new Cmd("stop");

        if (cmd.checkMess(mess)) {
            if (!memVoice.inVoiceChannel()) {
                channel.sendMessage("Bạn chưa vào kênh thoại").queue();
                return;
            }

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            musicManager.scheduler.setRepeating(false);
            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();

            channel.sendMessage("Dừng nhạc :stop_button:").queue();
        }
    }
}
