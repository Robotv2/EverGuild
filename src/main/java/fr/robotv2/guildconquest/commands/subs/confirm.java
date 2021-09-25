package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class confirm {

    private main main;
    public confirm(main main) {
        this.main = main;
    }

    public void onConfirm(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();

        if(!player.hasPermission("guild.command.confirm")) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(!main.getUtils().getConfirm().hasConfirmed(player)) {
            player.sendMessage(utilsGen.colorize(main.prefix + "&cVous n'avez aucune action à confirmer."));
            return;
        }

        main.getUtils().getConfirm().execute(player);
    }
}
