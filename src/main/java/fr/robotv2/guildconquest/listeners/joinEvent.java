package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class joinEvent implements Listener {

    private main main;
    public joinEvent(main main) {
        this.main = main;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        main.getMySQl().getGetter().createPlayer(player.getUniqueId());
        UUID uuid = main.getMySQl().getGetter().getGuildMysql(player);

        if(uuid != null)
            main.getUtils().getUtilsGuild().actualize(uuid);
    }
}
