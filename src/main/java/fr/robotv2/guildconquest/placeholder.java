package fr.robotv2.guildconquest;

import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGuild;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class placeholder extends PlaceholderExpansion {

    private main main;
    private utilsGuild utils;
    public placeholder(main main, utilsGuild utilsGuild) {
        this.main = main;
        this.utils = utilsGuild;
    }

    @Override
    public String getIdentifier() {
        return "guild";
    }

    @Override
    public  String getAuthor() {
        return "Robotv2";
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String placeholder) {
        switch(placeholder.toLowerCase()) {
            case "name":
                return getGuildName(player);
            case "point":
                return getPointGuild(player);
            case "level":
                return getLevelGuild(player);
            case "chef":
                return getChefGuild(player);
            case "uuid":
                return getUuidGuild(player);
        }
        return placeholder;
    }

    public boolean checks(OfflinePlayer OFplayer) {
        Player player = OFplayer.getPlayer();
        return player != null && utils.isInGuild(player);
    }

    public String getGuildName(OfflinePlayer OFplayer) {
        if(!checks(OFplayer)) return " ";
        Player player = OFplayer.getPlayer();
        Guild guild = utils.getGuild(player);
        return guild.getName();
    }

    public String getPointGuild(OfflinePlayer OFplayer) {
        if(!checks(OFplayer)) return " ";
        Player player = OFplayer.getPlayer();
        Guild guild = utils.getGuild(player);
        return String.valueOf(guild.getPoints());
    }

    public String getLevelGuild(OfflinePlayer OFplayer) {
        if(!checks(OFplayer)) return " ";
        Player player = OFplayer.getPlayer();
        Guild guild = utils.getGuild(player);
        return String.valueOf(guild.getLevel());
    }

    public String getChefGuild(OfflinePlayer OFplayer) {
        if(!checks(OFplayer)) return " ";
        Player player = OFplayer.getPlayer();
        Guild guild = utils.getGuild(player);
        return guild.getChef().getName();
    }

    public String getUuidGuild(OfflinePlayer OFplayer) {
        if(!checks(OFplayer)) return " ";
        Player player = OFplayer.getPlayer();
        Guild guild = utils.getGuild(player);
        return guild.getUuid().toString();
    }
}
