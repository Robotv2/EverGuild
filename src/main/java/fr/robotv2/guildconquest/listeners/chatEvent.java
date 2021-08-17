package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatEvent implements Listener {

    private main main;
    private utilsGuild utils;

    public chatEvent(main main, utilsGuild utilsGuild) {
        this.main = main;
        this.utils = utilsGuild;
    }

    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if(utils.isInGuildChat(player)) {
            e.setCancelled(true);
            utils.sendMessageToAllGuild(utils.getGuild(player),
                    player.getName() + ": " + e.getMessage(),
                    true);
        }
    }
}
