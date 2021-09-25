package fr.robotv2.guildconquest;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Iterables;
import com.onarandombox.MultiverseCore.MultiverseCore;
import fr.robotv2.guildconquest.commands.guildCommand;
import fr.robotv2.guildconquest.configs.terrDB;
import fr.robotv2.guildconquest.island.islandListeners;
import fr.robotv2.guildconquest.island.schematics.schemListeners;
import fr.robotv2.guildconquest.listeners.chatEvent;
import fr.robotv2.guildconquest.listeners.joinEvent;
import fr.robotv2.guildconquest.listeners.pluginMessageListener;
import fr.robotv2.guildconquest.listeners.quitEvent;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.territory.territoryManager;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.NoSuchElementException;

public final class main extends JavaPlugin {

    private utilsManager utilsManager;

    public String prefix = utilsGen.colorize("&e&lGUILD &7&l» ");
    public String channel = "guild:channel";

    private ProtocolManager manager;
    private territoryManager territoryManager;
    public MultiverseCore multiverseAPI = getPlugin(MultiverseCore.class);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        terrDB.setupDB();

        registerClasses();
        registerListeners();
        registerChannels();
        registerCommands();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new joinEvent(this), this);
        pm.registerEvents(new quitEvent(this), this);
        pm.registerEvents(new chatEvent(this), this);
        pm.registerEvents(new islandListeners(this), this);
        pm.registerEvents(new schemListeners(this), this);
    }

    public void registerClasses() {
        utilsManager = new utilsManager(this);
        manager = ProtocolLibrary.getProtocolManager();

        territoryManager = new territoryManager(this);
        getTerritory().registerUtils();
        getTerritory().registerListeners();
        getTerritory().initializeTerritories();

        new Guild(this);
        new placeholder(this).register();
    }

    public void registerChannels() {
        getServer().getMessenger().registerIncomingPluginChannel(this, channel, new pluginMessageListener(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, channel);
    }

    public void registerCommands() {
        getCommand("guild").setExecutor(new guildCommand(this));
        getCommand("guild").setTabCompleter(new guildCommand(this));
    }

    public utilsManager getUtils() {
        return utilsManager;
    }

    public territoryManager getTerritory() { return territoryManager; }

    public ProtocolManager getProtocol() { return manager; }

    public void sendDebug(String message) {
        if(getConfig().getBoolean("options.debug"))
            getLogger().info(utilsGen.colorize("&fDEBUG &7- " + message));
    }

    public Player getLast() {
        try {
            return Iterables.getLast(Bukkit.getOnlinePlayers());
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
