package fr.robotv2.guildconquest.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class utilsGen {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}
