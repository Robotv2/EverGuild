package fr.robotv2.guildconquest.object;

import fr.robotv2.guildconquest.main;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class Guild {

    private String name;
    private UUID uuid;
    private Double points;
    private int level;
    private OfflinePlayer chef;
    private List<OfflinePlayer> officier;
    private List<OfflinePlayer> membres;

    private main main;
    public Guild(main main) {
        this.main = main;
    }

    public Guild(String name, UUID uuid, Double points, int level, OfflinePlayer chef, List<OfflinePlayer> officier, List<OfflinePlayer> membres) {
        this.name = name;
        this.uuid = uuid;
        this.points = points;
        this.level = level;

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

    public int getLevel() { return level; }

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
}
