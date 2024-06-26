package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class create {

    private main main;
    public create(main main) {
        this.main = main;
    }

    public void onCreate(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(utilsGen.colorize("&cUSAGE: /guild create <nom>."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        String name = args[1];

        if(!player.hasPermission("guild.command.create")) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(utils.isInGuild(player))  {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous êtes déjà dans une guilde."));
            return;
        }

        utils.createGuild(name, player);
    }
}
