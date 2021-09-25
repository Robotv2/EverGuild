package fr.robotv2.guildconquest.territory.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.object.Territory;
import fr.robotv2.guildconquest.territory.fastboard.FastBoard;
import fr.robotv2.guildconquest.territory.utilsTerritory;
import net.raidstone.wgevents.WorldGuardEvents;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import net.raidstone.wgevents.events.RegionLeftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;

import static fr.robotv2.guildconquest.territory.territoryManager.boards;

public class regionListeners implements Listener {

    private main main;
    private final utilsTerritory util;
    public regionListeners(main main) {
        this.main = main;
        this.util = main
                .getTerritory()
                .getUtil();
    }

    public void enterInTerritory(Player player, Territory territoire) {
        if(player == null) return;
        if(territoire == null) return;

        territoire.addPlayer(player);
        territoire.getBar().addPlayer(player);
        boards.put(player.getUniqueId(), new FastBoard(player));
        territoire.actualizeScoreboardForPlayer(player);

        if(territoire.isWeak()) {
            util.sendTitle("&eTerritoire affaiblit.", "&7Ce territoire est affaiblit", player);
            return;
        }

        if(!territoire.isBossSpawned())
            territoire.spawnBoss();

        if(territoire.hasOwner()) {
            Guild guild = main.getUtils().getUtilsGuild().getGuild(territoire.getOwner());
            if(guild == null)
                main.getUtils().getUtilsGuild().actualizeForOnly(territoire.getOwner(), player);
            else
                util.sendTitle("&eTerritoire occupé", "&7Ce territoire est occupé par la guilde: " + guild.getName(), player);
        } else {
            util.sendTitle("&eTerritoire non-occupé", "&7Ce territoire n'est occupé par personne", player);
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Set<String> regions = WorldGuardEvents.getRegionsNames(player.getUniqueId());
        for(String region : regions) {
            if(!(region.startsWith("territory-"))) continue;
            String territoryStr = region.replace("territory-", "");
            Territory territoire = util.getTerritory(territoryStr);
            enterInTerritory(player, territoire);
        }
    }

    @EventHandler
    public void regionEntered(RegionEnteredEvent e) {
        String region = e.getRegionName();
        if(!(region.startsWith("territory-"))) return;

        String territoryStr = region.replace("territory-", "");
        Territory territoire = util.getTerritory(territoryStr);
        Player player = e.getPlayer();

        enterInTerritory(player, territoire);
    }

    @EventHandler
    public void regionLeft(RegionLeftEvent e) {
        String region = e.getRegionName();
        if(!(region.startsWith("territory-"))) return;

        String territoryStr = region.replace("territory-", "");
        Territory territoire = util.getTerritory(territoryStr);
        Player player = e.getPlayer();

        if(player == null) return;

        territoire.removePlayer(player);

        if(territoire.getBar().getPlayers().contains(player)) {
            territoire.getBar().removePlayer(player);
        }

        FastBoard board = boards.remove(player.getUniqueId());

        if (board != null)
            board.delete();
    }
}
