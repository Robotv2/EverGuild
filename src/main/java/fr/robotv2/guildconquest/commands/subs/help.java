package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.utils.utilsGen;
import org.bukkit.command.CommandSender;

public class help {

    public void onHelp(CommandSender sender, String[] args) {
        sender.sendMessage(utilsGen.colorize("&cà écrire :)"));
    }
}
