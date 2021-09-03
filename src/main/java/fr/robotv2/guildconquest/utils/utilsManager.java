package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.island.utilsIsland;
import fr.robotv2.guildconquest.main;

public class utilsManager {

    private utilsGuild utilsGuild;
    private utilsMessage utilsMessage;
    private utilsCache utilsCache;
    private utilsTop utilsTop;
    private utilsConfirm utilsConfirm;
    private utilsIsland utilsIsland;

    public utilsManager(main main) {
        this.utilsGuild = new utilsGuild(main);
        this.utilsMessage = new utilsMessage();
        this.utilsCache = new utilsCache(main);
        this.utilsTop = new utilsTop(main);
        this.utilsConfirm = new utilsConfirm(main);
        this.utilsIsland = new utilsIsland(main);
    }

    public utilsGuild getUtilsGuild() {
        return utilsGuild;
    }

    public utilsMessage getUtilsMessage() {
        return utilsMessage;
    }

    public utilsCache getCache() {
        return utilsCache;
    }

    public utilsTop getTopUtils() { return utilsTop; }

    public utilsConfirm getConfirm() { return utilsConfirm; }

    public utilsIsland getIsland() { return utilsIsland;}
}
