package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class joinEvent implements Listener {

    private main main;
    private utilsGuild utils;
    public joinEvent(main main) {
        this.main = main;
        this.utils = main.getUtils().getUtilsGuild();
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if(!main.getMySQl().isConnected()) autoReconnect();
        teleportPlayer(player);
    }

    public void autoReconnect() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!main.getMySQl().isConnected())
                    main.askSqlCredentials();
                else {
                    this.cancel();
                }
            }
        }.runTaskTimer(main, 0, 10);
    }

    public void teleportPlayer(Player player) {
        if(main.getUtils().getUtilsGuild().guildHome.containsKey(player.getUniqueId())) {
            Location home = main.getUtils().getUtilsGuild().guildHome.get(player.getUniqueId());
            player.teleport(home);
        }
    }
}
