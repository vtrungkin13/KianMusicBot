package KianBot;

import command.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.entities.Activity.listening;

public class Main {
    public final static String prefix = "$";

    public static void main(String[] args) throws LoginException {

        JDA jda = JDABuilder
                .createDefault("TOKEN")
                .enableCache(CacheFlag.VOICE_STATE)
                .build();

        jda.getPresence().setActivity(listening("Music || $help"));

//        jda.getPresence().setStatus(OnlineStatus.IDLE);
//        jda.getPresence().setActivity(Activity.playing("đang bảo trì"));

        jda.addEventListener(new ShutdownCommand());
        jda.addEventListener(new ServerWatching());

        jda.addEventListener(new JoinCommand());
        jda.addEventListener(new PlayCommand());
        jda.addEventListener(new SkipCommand());
        jda.addEventListener(new StopCommand());
        jda.addEventListener(new PauseCommand());
        jda.addEventListener(new ResumeCommand());
        jda.addEventListener(new NowPlayingCommand());
        jda.addEventListener(new QueueCommand());
//        jda.addEventListener(new LofiCommand());
        jda.addEventListener(new HelpCommand());
        jda.addEventListener(new PlayListCommand());
        jda.addEventListener(new AddPlayListCommand());
        jda.addEventListener(new AppendListCommand());
        jda.addEventListener(new RemoveListCommand());
        jda.addEventListener(new AutoLeaving());
        jda.addEventListener(new LoopCommand());
        jda.addEventListener(new Disconnect());

    }
}
