package fr.robotv2.guildconquest.commands.subs;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class chat {
    private main main;
    public chat(main main) {
        this.main = main;
    }

    public void onChat(CommandSender sender, String[] args) {
        if(!utilsGen.isPlayer(sender)) {
            sender.sendMessage(utilsGen.colorize("&cCette commande ne peut pas être exécutée depuis la console."));
            return;
        }

        Player player = (Player) sender;
        utilsGuild utils = main.getUtils().getUtilsGuild();
        Guild guild = utils.getGuild(player);

        if(!player.hasPermission("guild.command.chat")) {
            player.sendMessage(utilsGen.colorize("&cVous n'avez pas la permission d'exécuter cette commande."));
            return;
        }
        if(!utils.isInGuild(player))  {
            player.sendMessage(utilsGen.colorize("&cVous n'êtes dans aucune guilde."));
            return;
        }

        if(args.length == 0) utils.toggleGuildChat(player);

        StringBuilder sb = new StringBuilder();
        for(String arg : args) {
            if(arg.equalsIgnoreCase("chat")) continue;
            sb.append(arg + " ");
        }

        utils.sendMessageToAllGuild(guild, sb.toString(), true);
    }
}
