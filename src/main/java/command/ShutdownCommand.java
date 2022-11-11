package command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ShutdownCommand extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        Cmd shutdownCmd = new Cmd("off");

        if (shutdownCmd.checkMess(mess) && event.getAuthor().getId().equals("603492481597702144")) {
            event.getJDA().shutdown();
        }

    }
}