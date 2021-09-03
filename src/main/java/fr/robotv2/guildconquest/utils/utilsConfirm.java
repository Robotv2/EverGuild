package fr.robotv2.guildconquest.utils;

import fr.robotv2.guildconquest.main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class utilsConfirm {

    HashMap<Player, String> needConfirmation = new HashMap<>();

    private main main;
    public utilsConfirm(main main) {
        this.main = main;
    }

    public boolean hasConfirmed(Player player) {
        return needConfirmation.get(player) != null;
    }

    public void addConfirm(Player player, String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("guild ");
        for(String part : args) {
            sb.append(part).append(" ");
        }
        needConfirmation.put(player, sb.toString());
    }

    public String get(Player player) {
        return needConfirmation.get(player);
    }

    public void clear(Player player) {
        needConfirmation.remove(player);
    }

    public void execute(Player player) {
        Bukkit.dispatchCommand(player, get(player));
        clear(player);
    }
}
