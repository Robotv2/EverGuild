package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class utilsCache {

    private main main;
    public utilsCache(main main) {
        this.main = main;
    }

    public HashMap<UUID, UUID> cache = new HashMap<>();

    public void setCache(Player player, UUID guildUUID) {
        clearCache(player.getUniqueId());
        cache.put(player.getUniqueId(), guildUUID);
    }

    public UUID getCache(Player player) {
        return cache.get(player.getUniqueId());
    }

    public void clearCache(UUID playerUUID) {
        if(hasCache(playerUUID)) cache.remove(playerUUID);
    }

    public boolean hasCache(UUID playerUUD) {
        return cache.containsKey(playerUUD);
    }
}
