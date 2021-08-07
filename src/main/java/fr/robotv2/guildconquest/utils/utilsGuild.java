package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.guild;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class utilsGuild {

    public HashMap<UUID, guild> guildByUUID = new HashMap<>();

    private main main;
    public utilsGuild(main main) {
        this.main = main;
    }

    //GUILD FETCHER
    public guild getGuild(Player player) { return getGuild(main.getMySQl().getGetter().getGuildMysql(player)); }

    public guild getGuild(UUID uuid) {
        return guildByUUID.get(uuid);
    }

    //GUILD CREATOR
    public guild createGuild(String name, Player player) {
        UUID uuid = UUID.randomUUID();
        Double points = 0.0;
        List<OfflinePlayer> result = new ArrayList<>();
        result.add(player);

        return new guild(name, uuid, points, player, null, result);
    }
}
