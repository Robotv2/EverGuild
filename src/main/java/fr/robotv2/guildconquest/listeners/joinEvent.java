package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinEvent implements Listener {

    private main main;
    public joinEvent(main main) {
        this.main = main;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        teleportPlayer(player);
    }

    public void teleportPlayer(Player player) {
        if(main.getUtils().getUtilsGuild().guildHome.containsKey(player.getUniqueId())) {
            Location home = main.getUtils().getUtilsGuild().guildHome.get(player.getUniqueId());
            player.teleport(home);
        }
    }
}
