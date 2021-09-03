package fr.robotv2.guildconquest.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class chatEvent implements Listener {

    private main main;
    private utilsGuild utils;

    public chatEvent(main main) {
        this.main = main;
        this.utils = main.getUtils().getUtilsGuild();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        Guild guild = utils.getGuild(player);

        if(main.getUtils().getIsland().banned_player.contains(player.getUniqueId())) {
            e.setCancelled(true);
            main.getUtils().getIsland().addRestrictedPlayer(e.getMessage(), guild.getUuid(), player);
            main.getUtils().getIsland().banned_player.remove(player.getUniqueId());
            return;
        }

        if(utils.isInGuildChat(player) && guild != null) {
            e.setCancelled(true);
            utils.sendMessageToAllGuild(utils.getGuild(player).getUuid(),
                    utils.getRankStr(player, guild) + " &6" + player.getName() + "&8: &f" + e.getMessage(), true);
        }
    }
}
