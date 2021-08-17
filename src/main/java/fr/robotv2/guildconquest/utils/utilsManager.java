package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;

public class utilsManager {

    private utilsGuild utilsGuild;
    private utilsMessage utilsMessage;
    private utilsCache utilsCache;

    public utilsManager(main main) {
        this.utilsGuild = new utilsGuild(main);
        this.utilsMessage = new utilsMessage();
        this.utilsCache = new utilsCache(main);
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
}
