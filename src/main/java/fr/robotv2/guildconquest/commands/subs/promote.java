package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class promote {
    private main main;
    public promote(main main) {
        this.main = main;
    }

    public void onPromote(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);


        if(!player.hasPermission("guild.command.promote")) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(guild == null)  {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'êtes dans aucune guilde."));
            return;
        }
        if(!utils.isChef(guild, player)) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous devez être chef de la guilde pour pouvoir faire cette commande."));
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(utilsGen.colorize("&cUSAGE: /guild promote <nom>."));
            return;
        }
        String name = args[1];
        if(player.getName().equalsIgnoreCase(name)) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous ne pouvez pas vous promouvoir."));
            return;
        }
        OfflinePlayer OFplayer = Bukkit.getOfflinePlayer(name);
        if(!guild.getMembres().contains(OFplayer)) {
            main.sendDebug(guild.getMembres().toString());
            player.sendMessage(utilsGen.colorize(main.prefix + "&cCe joueur n'est pas dans votre guilde."));
            return;
        }
        if(guild.getOfficier().contains(OFplayer)) {
            main.sendDebug(guild.getOfficier().toString());
            player.sendMessage(utilsGen.colorize(main.prefix + "&cCe joueur est déjà officier de la guilde."));
            return;
        }

        utils.promotePlayer(guild.getUuid(), OFplayer.getUniqueId());
    }
}
