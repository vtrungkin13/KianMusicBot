package command;

import lavaplayer.GuildMusicManager;
import lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ResumeCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        final TextChannel channel = event.getChannel();

        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoice = self.getVoiceState();

        final Member member = event.getMember();
        final GuildVoiceState memVoice = member.getVoiceState();

        Cmd cmd = new Cmd("resume");

        if (cmd.checkMess(mess)) {
            if (!memVoice.inVoiceChannel()) {
                channel.sendMessage("Bạn chưa vào kênh thoại").queue();
                return;
            }

            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

            if (!musicManager.scheduler.player.isPaused()) {
                channel.sendMessage("Nhạc đã được phát").queue();
            } else {
                musicManager.scheduler.player.setPaused(false);
                channel.sendMessage("Tiếp tục phát nhạc :arrow_forward:").queue();
            }

        }
    }
}
