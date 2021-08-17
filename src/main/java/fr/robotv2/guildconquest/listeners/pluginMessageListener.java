package fr.robotv2.guildconquest.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class pluginMessageListener implements PluginMessageListener {

    //TO-USE
    private UUID playerUUID;
    private Player player;

    private UUID guildUUID;
    private Guild guild;

    private main main;
    public pluginMessageListener(main main) {
        this.main = main;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals(main.channel)) {
            final ByteArrayDataInput in = ByteStreams.newDataInput(message);
            final String sub = in.readUTF();

            switch (sub.toLowerCase()) {
                case "get-credentials":
                    guildUUID = UUID.fromString(in.readUTF());
                    String name = in.readUTF();
                    Double points = in.readDouble();
                    int level = in.readInt();

                    OfflinePlayer chef = Bukkit.getOfflinePlayer(UUID.fromString(in.readUTF()));
                    List<OfflinePlayer> officer = unformat(in.readUTF());
                    List<OfflinePlayer> membres = unformat(in.readUTF());

                    Guild guild = new Guild(name, guildUUID, points, level, chef, officer, membres);

                    main.sendDebug("Information d'un nouveau clan reçu: &6" + guild.getName() + " &8(&f" + guild.getUuid().toString() + "&8)");

                    utilsGuild utils = main.getUtils().getUtilsGuild();
                    if(utils.guildByUUID.containsKey(guildUUID))
                        utils.guildByUUID.remove(guildUUID);
                    utils.guildByUUID.put(guildUUID, guild);
                    return;

                case "get-mysql":
                    String host = in.readUTF();
                    String port  = in.readUTF();
                    String database = in.readUTF();
                    String username = in.readUTF();
                    String password = in.readUTF();
                    String ssl = in.readUTF();

                    main.getMySQl().initializeConnection(host, port, database, username, password, ssl);
                    return;

                case "invite-player":
                    guildUUID = UUID.fromString(in.readUTF()); //GUILD UUID
                    player = Bukkit.getPlayer(UUID.fromString(in.readUTF())); //PLAYER UUID

                    main.getUtils().getUtilsGuild().actualizeForOnly(guildUUID, player);
                    guild = main.getUtils().getUtilsGuild().getGuild(guildUUID);

                    player.sendMessage(utilsGen.colorize("&7Vous venez de reçevoir une invitation pour rejoindre la guilde: &f" + guild.getName()));
                    main.getUtils().getUtilsMessage().inviteAccept(player);
                    main.getUtils().getUtilsMessage().inviteDeny(player);
                    return;

                case "remove-guild":
                    guildUUID = UUID.fromString(in.readUTF()); //GUILD UUID
                    if(main.getUtils().getUtilsGuild().guildByUUID.containsValue(guildUUID)) {
                        main.getUtils().getUtilsGuild().guildByUUID.remove(guildUUID);
                    }
                    return;

                case "initialize-teleport":
                    playerUUID = UUID.fromString(in.readUTF());
                    player = Bukkit.getPlayer(playerUUID);

                    Double X = in.readDouble();
                    Double Y = in.readDouble();
                    Double Z = in.readDouble();
                    Float yaw = in.readFloat();
                    Float pitch = in.readFloat();
                    World world = Bukkit.getWorld(in.readUTF());

                    Location home = new Location(world, X, Y, Z, yaw, pitch);

                    if(player != null && player.isOnline()) {
                        player.teleport(home);
                    } else {
                        main.getUtils().getUtilsGuild().guildHome.put(playerUUID, home);
                    }
                    return;
                case "set-cache":
                    playerUUID = UUID.fromString(in.readUTF());
                    player = Bukkit.getPlayer(playerUUID);
                    guildUUID = UUID.fromString(in.readUTF());

                    main.getUtils().getCache().setCache(player, guildUUID);
                    return;
                case "clear-cache":
                    playerUUID = UUID.fromString(in.readUTF()); //PLAYER-UUID
                    main.getUtils().getCache().clearCache(playerUUID);
                    return;
            }
        }
    }

    public List<OfflinePlayer> unformat(String initial) {
        List<OfflinePlayer> result = new ArrayList<>();
        if(initial == null || initial.isEmpty()) return result;

        String[] list = initial.split(";");
        for(String playerStr : list) {
            OfflinePlayer OFplayer = Bukkit.getOfflinePlayer(UUID.fromString(playerStr));
            result.add(OFplayer);
        }
        return result;
    }
}