package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class invite {

    private main main;
    public invite(main main) {
        this.main = main;
    }

    public void onInvite(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(utilsGen.colorize("&cUSAGE: /guild invite <joueur>"));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);

        if(!player.hasPermission("guild.command.invite")) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(guild == null)  {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'êtes dans aucune guilde."));
            return;
        }
        if(!utils.isChef(guild, player) && !utils.isOfficier(guild, player)) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous devez être au minimum officier de la guilde pour faire cette commande."));
            return;
        }
        if(args[1].equalsIgnoreCase(player.getName())) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous ne pouvez pas vous inviter."));
            return;
        }
        utils.invitePlayer(guild, args[1], player);
    }
}
