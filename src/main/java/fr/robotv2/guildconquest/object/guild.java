package fr.robotv2.guildconquest.object;

import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class guild {

    private String name;
    private UUID uuid;
    private Double points;
    private OfflinePlayer chef;
    private List<OfflinePlayer> officier;
    private List<OfflinePlayer> membres;

    public guild(String name, UUID uuid, Double points, OfflinePlayer chef, List<OfflinePlayer> officier, List<OfflinePlayer> membres) {
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

    private void setMembres(List<OfflinePlayer> membres) {
        this.membres = membres;
    }

    public void addMembers(OfflinePlayer player, guild guild) {
        List<OfflinePlayer> members = getMembres();
        members.add(player);
        setMembres(members);
    }

    public void kickMembers(OfflinePlayer player, guild guild) {
        List<OfflinePlayer> members = getMembres();
        members.remove(player);
        setMembres(members);
    }
}
