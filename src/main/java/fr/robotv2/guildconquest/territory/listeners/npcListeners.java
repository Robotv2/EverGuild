package fr.robotv2.guildconquest.territory.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Territory;
import fr.robotv2.guildconquest.territory.utilsTerritory;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class npcListeners implements Listener {

    private final main main;
    private final utilsTerritory utils;
    public npcListeners(main main) {
        this.main = main;
        this.utils = main.getTerritory().getUtil();
    }

    @EventHandler
    public void onGuardAsk(NPCRightClickEvent e) {
        NPC npc = e.getNPC();

        if(!npc.data().has("territory")) return;

        String terrStr = npc.data().get("territory");
        Territory territoire = utils.getTerritory(terrStr);
        org.bukkit.entity.Player player = e.getClicker();
        UUID guildUUID = main.getUtils().getUtilsGuild().getGuild(player).getUuid();

        if(!territoire.isWeak() && territoire.hasOwner() && territoire.getOwner().equals(guildUUID)) {
            main.getTerritory().getUImanager().open(player, territoire);
        }
    }
}
