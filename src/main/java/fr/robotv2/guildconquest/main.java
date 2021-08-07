package fr.robotv2.guildconquest;

import fr.robotv2.guildconquest.MySQL.sql;
import fr.robotv2.guildconquest.listeners.joinEvent;
import fr.robotv2.guildconquest.listeners.pluginMessageListener;
import fr.robotv2.guildconquest.utils.utilsManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    public sql sql;
    public utilsManager utilsManager;

    @Override
    public void onEnable() {
        sql = new sql(this);
        utilsManager = new utilsManager(this);
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new joinEvent(this), this);

        getServer().getMessenger().registerIncomingPluginChannel(this, "guild:channel", new pluginMessageListener(this));
        getServer().getMessenger().registerOutgoingPluginChannel(this, "guild:channel");
    }

    public sql getMySQl() {
        return sql;
    }

    public utilsManager getUtils() {
        return utilsManager;
    }
}
