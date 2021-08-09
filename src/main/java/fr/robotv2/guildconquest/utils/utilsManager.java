package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;

public class utilsManager {

    public utilsGuild utilsGuild;
    public utilsMessage utilsMessage;

    public utilsManager(main main) {
        this.utilsGuild = new utilsGuild(main);
    }

    public utilsGuild getUtilsGuild() {
        return utilsGuild;
    }

    public utilsMessage getUtilsMessage() {
        return utilsMessage;
    }
}
