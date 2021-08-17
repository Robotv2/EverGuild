package fr.robotv2.guildconquest.commands;

import fr.robotv2.guildconquest.commands.subs.*;
import fr.robotv2.guildconquest.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class guildCommand implements CommandExecutor, TabCompleter {

    private main main;

    private create create;
    private delete delete;

    private invite invite;
    private kick kick;

    private accept accept;
    private deny deny;

    private help help;
    private chat chat;
    private info info;

    private sethome sethome;
    private home home;

    private leave leave;
    private promote promote;
    private demote demote;
    public guildCommand(main main) {
        this.main = main;

        this.create = new create(main);
        this.delete = new delete(main);
        this.invite = new invite(main);
        this.kick = new kick(main);
        this.accept = new accept(main);
        this.deny = new deny(main);
        this.info = new info(main);
        this.sethome = new sethome(main);
        this.home = new home(main);
        this.leave = new leave(main);
        this.chat = new chat(main);
        this.promote = new promote(main);
        this.demote = new demote(main);
        this.help = new help();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            this.help.onHelp(sender, args);
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
            case "accept":
                this.accept.onAccept(sender, args);
                return true;
            case "deny":
                this.deny.onDeny(sender, args);
                return true;
            case "home":
                this.home.onHome(sender, args);
                return true;
            case "sethome":
                this.sethome.onSetHome(sender, args);
                return true;
            case "info":
                this.info.onInfo(sender, args);
                return true;
            case "chat":
                this.chat.onChat(sender, args);
                return true;
            case "leave":
                this.leave.onLeave(sender, args);
                return true;
            case "promote":
                this.promote.onPromote(sender, args);
                return true;
            case "demote":
                this.demote.onDemote(sender, args);
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

        //TODO: à faire
    }
}
