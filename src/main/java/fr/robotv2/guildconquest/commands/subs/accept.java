package fr.robotv2.guildconquest.commands.subs;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class accept {
    private main main;
    public accept(main main) {
        this.main = main;
    }

    public void onAccept(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("guild.command.accept")) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("accept-player");
        out.writeUTF(player.getUniqueId().toString());

        main.getServer().sendPluginMessage(main, main.channel, out.toByteArray());
    }
}
