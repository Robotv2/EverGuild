package fr.robotv2.guildconquest.mysql;

import fr.robotv2.guildconquest.main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class getter {

    private main main;

    public getter(main main) {
        this.main = main;
    }

    public void createTable() {
        PreparedStatement ps;
        try {
            ps = main.getMySQl().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS guild_assignations " + "(UUID VARCHAR(100),GUILD VARCHAR(100),PRIMARY KEY (UUID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPlayer(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                if (!this.exists(uuid)) {
                    PreparedStatement ps = main.getMySQl().getConnection().prepareStatement("INSERT IGNORE INTO guild_assignations"
                            + " (UUID,GUILD) VALUES (?,?)");
                    ps.setString(1, uuid.toString());
                    ps.setString(2, "null");
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = main.getMySQl().getConnection().prepareStatement("SELECT * FROM  guild_assignations WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UUID getGuildMysql(OfflinePlayer player) {
        ResultSet rs = null;
        try {
            PreparedStatement ps = main.getMySQl().getConnection().prepareStatement("SELECT GUILD FROM guild_assignations WHERE UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next() && !rs.getString("GUILD").equalsIgnoreCase("null")) {
                return UUID.fromString(rs.getString("GUILD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setGuild(UUID uuid, OfflinePlayer player) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                PreparedStatement ps = main.getMySQl().getConnection().prepareStatement("UPDATE guild_assignations SET GUILD=? WHERE UUID=?");
                ps.setString(1, uuid.toString());
                ps.setString(2, player.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void clearGuild(UUID uuid) {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                PreparedStatement ps = main.getMySQl().getConnection().prepareStatement("UPDATE guild_assignations SET GUILD=? WHERE UUID=?");
                ps.setString(1, "null");
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
