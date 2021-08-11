package fr.robotv2.guildconquest.object;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import fr.robotv2.guildconquest.main;

import java.util.List;
import java.util.UUID;

public class Guild {

    private String name;
    private UUID uuid;
    private Double points;
    private OfflinePlayer chef;
    private List<OfflinePlayer> officier;
    private List<OfflinePlayer> membres;

    private main main;
    public Guild(main main) {
        this.main = main;
    }

    public Guild(String name, UUID uuid, Double points, OfflinePlayer chef, List<OfflinePlayer> officier, List<OfflinePlayer> membres) {
        this.name = name;
        this.uuid = uuid;
        this.points = points;

        this.chef = chef;
        this.officier = officier;
        this.membres = membres;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Double getPoints() {
        return points;
    }

    public OfflinePlayer getChef() {
        return chef;
    }

    public List<OfflinePlayer> getOfficier() {
        return officier;
    }

    public List<OfflinePlayer> getMembres() {
        return membres;
    }

    public int getSize() { return getMembres().size(); }

    public void addMembers(OfflinePlayer player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("add-member");
        out.writeUTF(getUuid().toString());
        out.writeUTF(player.getUniqueId().toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());

        //TODO
    }

    public void kickMembers(OfflinePlayer player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("kick-member");
        out.writeUTF(getUuid().toString());
        out.writeUTF(player.getUniqueId().toString());

        main.getServer().sendPluginMessage(main, "guild:channel", out.toByteArray());

        //TODO
    }
}
