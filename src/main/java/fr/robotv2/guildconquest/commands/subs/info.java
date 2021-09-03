package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class info {

    private main main;
    public info(main main) {
        this.main = main;
    }

    public void onInfo(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);

        if(!player.hasPermission("guild.command.info")) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(guild == null) {
            player.sendMessage(utilsGen.colorize("&cVous n'êtes dans aucune guilde."));
            return;
        }
        sendInfo(player, guild);
    }

    public void sendInfo(Player player, Guild guild) {
        player.sendMessage(utilsGen.colorize("&e&m------------------------------------"));
        player.sendMessage(utilsGen.colorize("&f&l» &e&lNOM: &f" + guild.getName()));
        player.sendMessage(utilsGen.colorize("&f&l» &e&lCHEF: §f" + guild.getChef().getName()));
        player.sendMessage(utilsGen.colorize("&f&l» &e&lMEMBRES: §f" + guild.getSize()));
        player.sendMessage(utilsGen.colorize("&f&l» &e&lPOINT(S): §f" + String.valueOf(guild.getPoints())));
        //player.sendMessage("§fPour voir les membres du gang: §e/gang list");
        player.sendMessage("§e§m------------------------------------");
    }
}
