package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class changename {

    private main main;
    public changename(main main) {
        this.main = main;
    }

    public void onChangeName(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(utilsGen.colorize("&cUSAGE: /guild changename <nom>."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);
        String name = args[1];

        if(!player.hasPermission("guild.command.changename")) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(guild == null)  {
            player.sendMessage(utilsGen.colorize("&cVous n'êtes dans aucune guilde."));
            return;
        }
        if(!utils.isChef(guild, player)) {
            player.sendMessage(utilsGen.colorize("&cVous devez être chef de la guilde pour pouvoir faire cette commande."));
            return;
        }

        utils.changeName(guild.getUuid(), player.getUniqueId(), name);
    }
}
