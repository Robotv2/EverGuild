package fr.robotv2.guildconquest.island.schematics;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class schemListeners implements Listener {

    private main main;
    private utilsSchem utils;
    public schemListeners(main main) {
        this.main = main;
        this.utils = main.getUtils().getIsland().getSchematics();

        main.getProtocol().addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();

                boolean isInRightMode = main.getUtils().getIsland().getSchematics().isInSchemMode(player);
                if(!isInRightMode) return;

                WrappedChatComponent wrappedMessage = event.getPacket().getChatComponents().read(0);
                if(wrappedMessage == null) {
                    event.setCancelled(true);
                }
            }
        });
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(utils.isInSchemMode(player) && player.isSneaking()) {
            utils.pasteSchem(player);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if(player.hasPermission("guild.island.bypass")) return;

        String cmd = e.getMessage();

        if(cmd.startsWith("//undo") || cmd.startsWith("/undo")) {
                if(!utils.undo.containsKey(player.getUniqueId())) {
                    player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez rien a undo."));
                    e.setCancelled(true);
                    return;
                }
                utils.schematics.put(player.getUniqueId(), "to-undo");
                String schem = utils.undo.get(player.getUniqueId());
                utils.undo.remove(player.getUniqueId());
                utils.addSchem(player, schem, 1);
                utils.schematics.remove(player.getUniqueId());
        }
        else if(cmd.startsWith("//paste")) {
            if(!utils.paste.contains(player)) {
                e.setCancelled(true);
            }
        }
        else if(cmd.startsWith("//schem") || cmd.startsWith("/schem")) {
            if(!utils.load.contains(player)) {
                e.setCancelled(true);
            }
        }
        else if(cmd.startsWith("/rotate")) {
            if(!utils.schematics.containsKey(player.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
}
