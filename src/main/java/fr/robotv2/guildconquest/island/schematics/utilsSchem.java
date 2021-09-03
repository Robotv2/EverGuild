package fr.robotv2.guildconquest.island.schematics;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class utilsSchem {

    private main main;
    public HashMap<UUID, String> schematics = new HashMap<>();
    public HashMap<UUID, String> undo = new HashMap<>();

    public List<Player> load = new ArrayList<>();
    public List<Player> paste = new ArrayList<>();

    public utilsSchem(main main) {
        this.main = main;
    }

    public void initSchem(Player player, String schem) {
        schematics.remove(player.getUniqueId());
        schematics.put(player.getUniqueId(), schem);
    }

    public void loadSchem(Player player) {
        if(!main.getUtils().getIsland().isInGuildIsland(player)) {
            player.sendMessage(utilsGen.colorize("&cVous ne pouvez pas faire ça ici."));
            return;
        }
        if(getCurrentSchem(player) == null) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas de schematics en cours."));
            return;
        }

        load.add(player);
        Bukkit.dispatchCommand(player, "schem load island-" + getCurrentSchem(player));
        player.sendMessage(utilsGen.colorize("&7Votre schematics a bien été chargé."));
        load.remove(player);
    }

    public void paste(Player player, boolean can) {
        if(!can) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez plus accès à ce schematics."));
            schematics.remove(player.getUniqueId());
            return;
        }

        undo.put(player.getUniqueId(), getCurrentSchem(player));
        removeSchem(player, getCurrentSchem(player), 1);

        paste.add(player);
        Bukkit.dispatchCommand(player, "/paste -a");
        player.sendMessage(utilsGen.colorize("&7Vous venez de placer votre schematics. Vous pouvez utiliser le &e//undo &7pour recommencer."));

        paste.remove(player);
        schematics.remove(player.getUniqueId());
    }

    public String getCurrentSchem(Player player) {
        return schematics.get(player.getUniqueId());
    }

    public boolean isInSchemMode(Player player) {
        return getCurrentSchem(player) != null;
    }

    public void pasteSchem(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("paste-schem");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(getCurrentSchem(player));

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void addSchem(Player sender, String schem, int count) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("add-schem");
        out.writeUTF(sender.getUniqueId().toString());
        out.writeUTF(schem);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void removeSchem(Player sender, String schem, int count) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("remove-schem");
        out.writeUTF(sender.getUniqueId().toString());
        out.writeUTF(schem);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }
}
