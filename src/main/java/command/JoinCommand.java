package command;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        Cmd joinCmd = new Cmd("summon");

        if (joinCmd.checkMess(mess)) {
            final TextChannel channel = event.getChannel();
            final Member self = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoice = self.getVoiceState();
            if (selfVoice.inVoiceChannel()) {
                return;
            }

            final Member member = event.getMember();
            final GuildVoiceState memVoice = member.getVoiceState();
            if (!memVoice.inVoiceChannel()) {
                channel.sendMessage("Bạn chưa vào kênh thoại").queue();
                return;
            }
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memChannel = memVoice.getChannel();
            audioManager.openAudioConnection(memChannel);
            channel.sendMessage("Chọn nhạc để phát [play + link/tênbàihát]").queue();
        }
    }

}
