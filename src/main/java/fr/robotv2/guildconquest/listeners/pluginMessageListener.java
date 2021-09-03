package fr.robotv2.guildconquest.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.island.utilsIsland;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import fr.robotv2.guildconquest.utils.utilsTop;
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

            main.sendDebug("Nouveau message reçu: " + sub);
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

                    main.sendDebug("&7Information d'un nouveau clan reçu: &6" + guild.getName() + " &8(&f" + guild.getUuid().toString() + "&8)");

                    utilsGuild utils = main.getUtils().getUtilsGuild();
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
                    main.getUtils().getIsland().deleteWorld(guildUUID);
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
                    guildUUID = UUID.fromString(in.readUTF());

                    main.getUtils().getCache().setCache(playerUUID, guildUUID);
                    return;

                case "clear-cache":
                    playerUUID = UUID.fromString(in.readUTF()); //PLAYER-UUID
                    main.getUtils().getCache().clearCache(playerUUID);
                    return;

                case "set-top":
                    int position = in.readInt();
                    guildUUID = UUID.fromString(in.readUTF());
                    utilsTop.topType type = (in.readUTF().equalsIgnoreCase("level") ? utilsTop.topType.LEVEL : utilsTop.topType.POINTS);
                    main.getUtils().getTopUtils().setTop(position, guildUUID, type);
                    return;

                case "create-island":
                    guildUUID = UUID.fromString(in.readUTF());
                    String npcLocation = in.readUTF();
                    World template = Bukkit.getWorld("island-template");

                    main.getUtils().getIsland().createWorld(template, "island-" + guildUUID.toString());
                    main.getUtils().getIsland().initializeNPC(guildUUID, npcLocation);
                    main.getUtils().getIsland().initializeChest(guildUUID);
                    return;

                case "open-inventory-list":
                    playerUUID = UUID.fromString(in.readUTF());
                    String island = in.readUTF();
                    int page = in.readInt();
                    String initial = in.readUTF();
                    main.getUtils().getIsland().getGui().openInventoryList(player, utilsIsland.islandType.valueOf(island), page, initial);
                    return;

                case "open-banned-list":
                    playerUUID = UUID.fromString(in.readUTF());
                    page = in.readInt();
                    initial = in.readUTF();
                    boolean isRestricted = in.readBoolean();

                    player = Bukkit.getPlayer(playerUUID);
                    main.getUtils().getIsland().getGui().openBannedPlayersInventory(player, page, initial, isRestricted);
                    return;

                case "open-schem-shop":
                    playerUUID = UUID.fromString(in.readUTF());
                    initial = in.readUTF();
                    page = in.readInt();

                    player = Bukkit.getPlayer(playerUUID);
                    main.getUtils().getIsland().getGui().openSchemShop(player, page, initial);
                    return;

                case "open-schem-stock":
                    playerUUID = UUID.fromString(in.readUTF());
                    initial = in.readUTF();

                    player = Bukkit.getPlayer(playerUUID);
                    main.getUtils().getIsland().getGui().openSchemList(player, initial);
                    return;

                case "paste-schem":
                    playerUUID = UUID.fromString(in.readUTF());
                    boolean can = in.readBoolean();

                    player = Bukkit.getPlayer(playerUUID);
                    main.getUtils().getIsland().getSchematics().paste(player, can);
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