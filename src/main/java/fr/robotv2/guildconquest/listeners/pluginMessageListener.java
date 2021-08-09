package fr.robotv2.guildconquest.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGuild;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class pluginMessageListener implements PluginMessageListener {

    private main main;
    public pluginMessageListener(main main) {
        this.main = main;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equalsIgnoreCase("guild:channel")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String sub = in.readUTF();

            switch (sub.toLowerCase()) {
                case "get-credentials":
                    UUID uuid = UUID.fromString(in.readUTF());
                    String name = in.readUTF();
                    Double points = in.readDouble();

                    OfflinePlayer chef = getOfflinePlayer(in.readUTF());
                    List<OfflinePlayer> officer = unformat(in.readUTF());
                    List<OfflinePlayer> membres = unformat(in.readUTF());

                    Guild guild = new Guild(name, uuid, points, chef, officer, membres);

                    utilsGuild utilsGuild = main.getUtils().getUtilsGuild();
                    if(utilsGuild.guildByUUID.containsKey(uuid))
                        utilsGuild.guildByUUID.remove(uuid);
                    utilsGuild.guildByUUID.put(uuid, guild);
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