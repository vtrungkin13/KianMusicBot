package command;

import static KianBot.Main.prefix;

public class Cmd {
    private String name;
    private String[] aliases;

    public Cmd() {
    }

    public Cmd(String name) {
        this.name = name;
    }

    public Cmd(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public boolean checkMess(String message) {
        if (message.equalsIgnoreCase(prefix + name))
            return true;
        else {
            for (String item: aliases) {
                if (message.equalsIgnoreCase(prefix + item))
                    return true;
            }
            return false;
        }
    }
}
