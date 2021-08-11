package fr.robotv2.guildconquest.commands;

import fr.robotv2.guildconquest.commands.subs.*;
import fr.robotv2.guildconquest.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class guildCommand implements CommandExecutor, TabCompleter {

    private main main;

    private create create;
    private delete delete;
    private invite invite;
    private kick kick;
    private help help;
    public guildCommand(main main) {
        this.main = main;

        this.create = new create(main);
        this.delete = new delete(main);
        this.invite = new invite(main);
        this.kick = new kick(main);
        this.help = new help();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            help.onHelp(sender, args);
            return true;
        }
        switch(args[0].toLowerCase()) {
            case "create":
                this.create.onCreate(sender, args);
                return true;
            case "delete":
                this.delete.onDelete(sender, args);
                return true;
            case "invite":
                this.invite.onInvite(sender, args);
                return true;
            case "kick":
                this.kick.onKick(sender, args);
                return true;
            case "help":
                this.help.onHelp(sender, args);
                return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s,  String[] strings) {
        return null;
    }
}
