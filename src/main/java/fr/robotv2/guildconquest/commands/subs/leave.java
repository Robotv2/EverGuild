package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class leave {
    private main main;
    public leave(main main) {
        this.main = main;
    }

    public void onLeave(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);

        if(!player.hasPermission("guild.command.leave")) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(guild == null)  {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'êtes dans aucune guilde."));
            return;
        }
        if(utils.isChef(guild, player)) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous ne pouvez quitter la guilde car vous êtes le chef."));
            player.sendMessage(utilsGen.colorize(main.prefix + "&cSi vous voulez supprimer la guilde: /guild delete."));
            return;
        }
        if(!main.getUtils().getConfirm().hasConfirmed(player)) {
            main.getUtils().getConfirm().addConfirm(player, args);
            main.getUtils().getUtilsMessage().sendConfirmation(player);
            return;
        }

        utils.leaveGuild(player, guild);
    }
}
