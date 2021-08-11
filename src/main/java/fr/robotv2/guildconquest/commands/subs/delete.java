package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class delete {

    private main main;
    public delete(main main) {
        this.main = main;
    }

    public void onDelete(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);

        if(!player.hasPermission("guild.command.delete")) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(!utils.isInGuild(player))  {
            player.sendMessage(utilsGen.colorize("&cVous n'êtes dans aucune guilde."));
            return;
        }
        if(!utils.isChef(guild, player)) {
            player.sendMessage(utilsGen.colorize("&cVous devez être chef de la guilde pour pouvoir faire cette commande."));
            return;
        }

        utils.removeGuild(guild);
        player.sendMessage(utilsGen.colorize("&7Vous venez de supprimer votre guilde."));
    }
}
