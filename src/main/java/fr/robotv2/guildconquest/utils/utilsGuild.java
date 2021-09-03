package fr.robotv2.guildconquest.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class utilsGuild {

    public HashMap<UUID, Guild> guildByUUID = new HashMap<>();
    public HashMap<UUID, Location> guildHome = new HashMap<>();
    List<Player> inGuildChat = new ArrayList<>();

    private main main;
    public utilsGuild(main main) {
        this.main = main;
    }

    //GUILD FETCHER
    public Guild getGuild(OfflinePlayer player) {
        if(player.isOnline())
            return getGuild(main.getUtils().getCache().getCache(player.getPlayer()));
        else
            return getGuild(main.getMySQl().getGetter().getGuildMysql(player));
    }

    public Guild getGuild(UUID uuid) {
        if (uuid == null) return null;
        return guildByUUID.get(uuid);
    }

    //GUILD CREATOR
    public void createGuild(String name, Player player) {
        UUID uuid = UUID.randomUUID();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("create-guild");
        out.writeUTF(uuid.toString()); //GUILD UUID
        out.writeUTF(name); //NOM
        out.writeUTF(player.getUniqueId().toString()); //UUID DU CHEF

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    //GUILD REMOVER
    public void removeGuild(Guild guild) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("remove-guild");
        out.writeUTF(guild.getUuid().toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void actualize(UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("get-credentials");
        out.writeUTF(uuid.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void actualizeForOnly(UUID uuid, Player sender) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("get-credentials-for-only");
        out.writeUTF(uuid.toString());
        out.writeUTF(sender.getUniqueId().toString());

        sender.sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void invitePlayer(Guild guild, String playerName, Player sender) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("invite-player");
        out.writeUTF(guild.getUuid().toString());
        out.writeUTF(playerName);
        out.writeUTF(sender.getUniqueId().toString());

        sender.sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void kickPlayer(Guild guild, UUID playerUUID) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("kick-player");
        out.writeUTF(guild.getUuid().toString());
        out.writeUTF(playerUUID.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void teleportToHome(Player player) {
        UUID uuid = main.getUtils().getCache().getCache(player);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("home-guild");
        out.writeUTF(uuid.toString());
        out.writeUTF(player.getUniqueId().toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void sethomeGuild(Player player, Guild guild) {
        Location loc = player.getLocation();
        Double X = loc.getX();
        Double Y = loc.getY();
        Double Z = loc.getZ();
        Float yaw = loc.getYaw();
        Float pitch = loc.getPitch();
        String world = loc.getWorld().getName();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("sethome-guild");
        out.writeUTF(guild.getUuid().toString());
        out.writeUTF(player.getUniqueId().toString());

        out.writeDouble(X);
        out.writeDouble(Y);
        out.writeDouble(Z);
        out.writeFloat(yaw);
        out.writeFloat(pitch);
        out.writeUTF(world);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void leaveGuild(Player player, Guild guild) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("leave-guild");
        out.writeUTF(guild.getUuid().toString());
        out.writeUTF(player.getUniqueId().toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void sendMessageToAllGuild(UUID guildUUID, String message, boolean prefix) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("message-guild");
        out.writeUTF(guildUUID.toString());
        out.writeUTF(message);
        out.writeBoolean(prefix);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void promotePlayer(UUID guildUUID, UUID playerUUID) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("promote-player");
        out.writeUTF(guildUUID.toString());
        out.writeUTF(playerUUID.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void demotePlayer(UUID guildUUID, UUID playerUUID) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("demote-player");
        out.writeUTF(guildUUID.toString());
        out.writeUTF(playerUUID.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void changeName(UUID guildUUID, UUID playerUUID, String newName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("change-name");
        out.writeUTF(guildUUID.toString());
        out.writeUTF(playerUUID.toString());
        out.writeUTF(newName);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public String getRankStr(Player player, Guild guild) {
        if(isChef(guild, player)) return "&c&lCHEF";
        if(isOfficier(guild, player)) return "&e&lOFFICIER";
        if(isMembre(guild, player)) return "&7&lMEMBRE";
        return "&8&lINVITE";
    }

    public boolean isChef (Guild guild, Player player) {
        return guild.getChef().equals(player);
    }

    public boolean isOfficier (Guild guild, Player player) {
        return guild.getOfficier().contains(player);
    }

    public boolean isMembre (Guild guild, Player player) {
        return guild.getMembres().contains(player);
    }

    public boolean isInGuild (Player player) {
        return getGuild(player) != null;
    }

    public boolean isInGuildChat(Player player) {
        return inGuildChat.contains(player);
    }

    public void toggleGuildChat(Player player) {
        if(isInGuildChat(player)) {
            inGuildChat.remove(player);
            player.sendMessage(utilsGen.colorize("&7Vous venez de &cdésactiver &7le chat de guilde."));
        } else {
            inGuildChat.add(player);
            player.sendMessage(utilsGen.colorize("&7Vous venez de &ad'activer &7le chat de guilde."));
        }
    }
}
