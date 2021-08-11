package fr.robotv2.guildconquest.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class utilsGuild {

    public HashMap<UUID, Guild> guildByUUID = new HashMap<>();
    public HashMap<Player, Guild> inInvite = new HashMap<>();

    private main main;
    public utilsGuild(main main) {
        this.main = main;
    }

    //GUILD FETCHER
    public Guild getGuild(Player player) { return getGuild(main.getMySQl().getGetter().getGuildMysql(player)); }

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

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
    }

    //GUILD REMOVER
    public void removeGuild(Guild guild) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("remove-guild");
        out.writeUTF(guild.getUuid().toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
    }

    public void actualize(UUID uuid) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("get-credentials");
        out.writeUTF(uuid.toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
    }

    public void invitePlayer(Guild guild, String playerName, Player sender) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("invite-player");
        out.writeUTF(guild.getUuid().toString());
        out.writeUTF(playerName);
        out.writeUTF(sender.getUniqueId().toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
    }

    public void kickPlayer(Guild guild, String playerUuidStr) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("kick-player");
        out.writeUTF(guild.getUuid().toString());
        out.writeUTF(playerUuidStr);

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
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
        return main.getMySQl().getGetter().getGuildMysql(player) != null;
    }

    public boolean exist (String name) {
        return false;
    }
}
