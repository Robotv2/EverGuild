package fr.robotv2.guildconquest;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.mysql.sql;
import fr.robotv2.guildconquest.commands.guildCommand;
import fr.robotv2.guildconquest.listeners.chatEvent;
import fr.robotv2.guildconquest.listeners.joinEvent;
import fr.robotv2.guildconquest.listeners.pluginMessageListener;
import fr.robotv2.guildconquest.listeners.quitEvent;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import fr.robotv2.guildconquest.utils.utilsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    public sql sql;
    public utilsManager utilsManager;

    public String channel = "guild:channel";

    @Override
    public void onEnable() {
        registerListeners();
        registerClasses();
        registerChannels();
        registerCommands();

        saveDefaultConfig();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new joinEvent(this), this);
        pm.registerEvents(new quitEvent(this), this);
        pm.registerEvents(new chatEvent(this), this);
    }

    public void registerClasses() {
        sql = new sql(this);
        utilsManager = new utilsManager(this);
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

    public sql getMySQl() {
        return sql;
    }

    public utilsManager getUtils() {
        return utilsManager;
    }

    public void askSqlCredentials() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("get-mysql");
        this.getLast().sendPluginMessage(this, channel, out.toByteArray());
        sendDebug("&6message envoyé: get-mysql");
    }

    public void sendDebug(String message) {
        if(getConfig().getBoolean("options.debug"))
            getLogger().info(utilsGen.colorize("&fDEBUG &7- " + message));
    }

    public Player getLast() {
        return Iterables.getLast(Bukkit.getOnlinePlayers());
    }
}
