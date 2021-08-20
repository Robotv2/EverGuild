package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;

public class utilsTop {

    public LinkedList<Guild> topLevel = new LinkedList<>();
    public LinkedList<Guild> topPoint = new LinkedList<>();

    public Guild guild;

    private main main;
    public utilsTop(main main) {
        this.main = main;
    }

    public enum topType {
        LEVEL, POINTS;
    }

    public void setTop(int position, UUID guildUUID, topType type) {
        Player any = main.getLast();
        utilsGuild util = main.getUtils().getUtilsGuild();
        this.guild = util.getGuild(guildUUID);

        if(any == null) return;
        if(guild == null) util.actualizeForOnly(guildUUID, any);

        Bukkit.getScheduler().runTaskLater(main, () -> {
            guild = util.getGuild(guildUUID);
            if(type == topType.LEVEL) topLevel.add(position, guild);
            else topPoint.add(position, guild);
        }, 3L);
    }
}
