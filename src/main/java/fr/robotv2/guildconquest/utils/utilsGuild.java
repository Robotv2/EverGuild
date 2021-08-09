package fr.robotv2.guildconquest.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class utilsGuild {

    public HashMap<UUID, Guild> guildByUUID = new HashMap<>();

    private main main;
    public utilsGuild(main main) {
        this.main = main;
    }

    //GUILD FETCHER
    public Guild getGuild(Player player) { return getGuild(main.getMySQl().getGetter().getGuildMysql(player)); }

    public Guild getGuild(UUID uuid) {
        return guildByUUID.get(uuid);
    }

    //GUILD CREATOR
    public void createGuild(String name, Player player) {
        UUID uuid = UUID.randomUUID();

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("create-guild");
        out.writeUTF(uuid.toString());
        out.writeUTF(name);
        out.writeUTF(player.getUniqueId().toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());
    }

    //GUILD REMOVER
    public void removeGuild(Guild guild) {
        if(guildByUUID.containsValue(guild.getUuid())) { guildByUUID.remove(guild.getUuid()); }

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
