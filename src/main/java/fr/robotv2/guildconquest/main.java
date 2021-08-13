package fr.robotv2.guildconquest;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.MySQL.sql;
import fr.robotv2.guildconquest.commands.guildCommand;
import fr.robotv2.guildconquest.listeners.joinEvent;
import fr.robotv2.guildconquest.listeners.pluginMessageListener;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    public sql sql;
    public utilsManager utilsManager;

    @Override
    public void onEnable() {
        registerClasses();
        registerListeners();
        registerChannels();
        registerCommands();

        askSqlCredentials();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new joinEvent(this), this);
    }

    public void registerClasses() {
        sql = new sql(this);
        utilsManager = new utilsManager(this);
        new Guild(this);
    }

    public void registerChannels() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "guild:channel", new pluginMessageListener(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, "guild:channel");
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
        getServer().sendPluginMessage(this, "guild:channel", out.toByteArray());
    }
}
