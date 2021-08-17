package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class quitEvent implements Listener {

    private fr.robotv2.guildconquest.main main;
    public quitEvent(main main) {
        this.main = main;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        main.getUtils().getCache().clearCache(e.getPlayer().getUniqueId());
    }
}
