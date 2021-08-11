package fr.robotv2.guildconquest.commands.subs;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class deny {

    private main main;
    public deny(main main) {
        this.main = main;
    }

    public void onDeny(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("guild.command.deny")) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("deny-player");
        out.writeUTF(player.getUniqueId().toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
    }
}
