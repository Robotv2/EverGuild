package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatEvent implements Listener {

    private main main;
    private utilsGuild utils;

    public chatEvent(main main) {
        this.main = main;
        this.utils = main.getUtils().getUtilsGuild();
    }


    // [CINESTIA] Officier Robotv2: salut toi
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Guild guild = utils.getGuild(player);
        if(utils.isInGuildChat(player)) {
            e.setCancelled(true);
            utils.sendMessageToAllGuild(utils.getGuild(player),
                    utils.getRankStr(player, guild) + " &6" + player.getName() + "&8: &f" + e.getMessage(), true);
        }
    }
}
