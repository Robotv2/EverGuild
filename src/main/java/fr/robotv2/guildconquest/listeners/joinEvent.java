package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.UUID;

public class joinEvent implements Listener {

    private main main;
    public joinEvent(main main) {
        this.main = main;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Long delay = 0L;

        if(!main.getMySQl().isConnected()) {
            delay = 20L;
            startAutoTryConnect();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                main.getMySQl().getGetter().createPlayer(player.getUniqueId());
                UUID uuid = main.getMySQl().getGetter().getGuildMysql(player);

                if(uuid != null)
                    main.getUtils().getUtilsGuild().actualize(uuid);
            }
        }.runTaskLater(main, delay);
    }

    public void startAutoTryConnect() {
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
}
