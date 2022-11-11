package command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Member;

import static KianBot.Main.prefix;

public class ServerWatching extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String mess = event.getMessage().getContentRaw();

        if (mess.equals(prefix + "server") && event.getAuthor().getId().equals("603492481597702144")) {
            for (int i=0; i< event.getJDA().getGuilds().size(); i++) {
                event.getChannel().sendMessage(event.getJDA().getGuilds().get(i).getName()).queue();
            }
        }

        if (mess.equals(prefix + "count") && event.getAuthor().getId().equals("603492481597702144")) {
            event.getChannel().sendMessage(Integer.toString(event.getJDA().getGuilds().size())).queue();
        }
    }
}
