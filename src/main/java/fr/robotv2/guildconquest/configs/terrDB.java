package fr.robotv2.guildconquest.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


public class terrDB {

    private static File database;
    private static FileConfiguration databaseconfig;

    public static void setupDB() {
        database = new File(Bukkit.getServer().getPluginManager().getPlugin("EverGuild").getDataFolder(), "territory.yml");
        if(!database.exists()) {
            try {
                if(!database.getParentFile().exists())
                    database.getParentFile().mkdir();
                database.createNewFile();
            } catch(IOException ignored) {
            }
        }
        databaseconfig = YamlConfiguration.loadConfiguration(database);
    }

    public static FileConfiguration getDB() {
        return databaseconfig;
    }

    public static void saveDB() {
        try {
            databaseconfig.save(database);
        } catch (IOException ignored) {
        }
    }

    public static void reloadDB() {
        databaseconfig = YamlConfiguration.loadConfiguration(database);
    }
}
