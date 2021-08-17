package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class utilsCache {

    private main main;
    public utilsCache(main main) {
        this.main = main;
    }

    public void setCache(Player player, UUID guildUUID) {
        main.getConfig().set("cache." + player.getUniqueId().toString(), guildUUID.toString());
    }

    public UUID getCache(Player player) {
        String UUIDstr = main.getConfig().getString("cache." + player.getUniqueId().toString());
        if(UUIDstr == null) return null;
        return UUID.fromString(UUIDstr);
    }

    public void clearCache(UUID uuid) {
        main.getConfig().set("cache." + uuid.toString(), null);
    }


}
