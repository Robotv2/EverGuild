package fr.robotv2.guildconquest.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class pluginMessageListener implements PluginMessageListener {

    //TO-USE
    private Player player;
    private UUID guildUUID;
    private Guild guild;

    private main main;
    public pluginMessageListener(main main) {
        this.main = main;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("guild:channel")) {
            final ByteArrayDataInput in = ByteStreams.newDataInput(message);
            final String sub = in.readUTF();

            switch (sub.toLowerCase()) {
                case "get-credentials":
                    guildUUID = UUID.fromString(in.readUTF());
                    String name = in.readUTF();
                    Double points = in.readDouble();

                    OfflinePlayer chef = getOfflinePlayer(in.readUTF());
                    List<OfflinePlayer> officer = unformat(in.readUTF());
                    List<OfflinePlayer> membres = unformat(in.readUTF());

                    Guild guild = new Guild(name, guildUUID, points, chef, officer, membres);

                    utilsGuild utilsGuild = main.getUtils().getUtilsGuild();
                    if(utilsGuild.guildByUUID.containsKey(guildUUID))
                        utilsGuild.guildByUUID.remove(guildUUID);
                    utilsGuild.guildByUUID.put(guildUUID, guild);
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

                    main.getUtils().getUtilsGuild().actualize(guildUUID);
                    Guild guild1 = main.getUtils().utilsGuild.getGuild(guildUUID);

                    player.sendMessage(utilsGen.colorize("&7Vous venez de reçevoir une invitation pour rejoindre la guilde: &f" + guild1.getName()));
                    main.getUtils().getUtilsMessage().inviteAccept(player);
                    main.getUtils().getUtilsMessage().inviteDeny(player);
                    return;

                case "remove-guild":
                    guildUUID = UUID.fromString(in.readUTF()); //GUILD UUID
                    if(main.getUtils().getUtilsGuild().guildByUUID.containsValue(guildUUID)) {
                        main.getUtils().getUtilsGuild().guildByUUID.remove(guildUUID);
                    }
                    return;
            }
        }
    }

    public List<OfflinePlayer> unformat(String initial) {
        List<OfflinePlayer> result = new ArrayList<>();
        String[] list = initial.split(";");
        Arrays.stream(list).forEach(uuid -> result.add(getOfflinePlayer(uuid)));
        return result;
    }

    public OfflinePlayer getOfflinePlayer(String uuidStr) {
        UUID uuid = UUID.fromString(uuidStr);
        return Bukkit.getOfflinePlayer(uuid);
    }
}