package fr.robotv2.guildconquest.territory;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Territory;
import fr.robotv2.guildconquest.territory.fastboard.FastBoard;
import fr.robotv2.guildconquest.territory.listeners.bossListeners;
import fr.robotv2.guildconquest.territory.listeners.npcListeners;
import fr.robotv2.guildconquest.territory.listeners.regionListeners;
import fr.robotv2.guildconquest.territory.ui.ItemAPI;
import fr.robotv2.guildconquest.territory.ui.UImanager;
import org.bukkit.plugin.PluginManager;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class territoryManager {

    private final main main;
    public utilsTerritory utilsTerritory;
    public UImanager UImanager;

    public static Map<UUID, FastBoard> boards = new HashMap<>();

    public territoryManager(main main) {
        this.main = main;
    }

    public void registerListeners() {
        PluginManager pm = main.getServer().getPluginManager();
        pm.registerEvents(new bossListeners(main), main);
        pm.registerEvents(new npcListeners(main), main);
        pm.registerEvents(new regionListeners(main), main);

        UImanager = new UImanager(main);
        pm.registerEvents(UImanager, main);
    }

    public void registerUtils() {
        utilsTerritory = new utilsTerritory(main);
        new ItemAPI(main);
    }

    public utilsTerritory getUtil() {
        return utilsTerritory;
    }

    public void initializeTerritories() {
        for(Territory territoire : getUtil().getTerritories()) {
            territoire.init();
        }
    }

    public UImanager getUImanager() { return UImanager; }
}
