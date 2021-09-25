package fr.robotv2.guildconquest.territory;

import fr.robotv2.guildconquest.configs.terrDB;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Territory;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fr.robotv2.guildconquest.utils.utilsGen.colorize;

public class utilsTerritory {

    private main main;
    public utilsTerritory(main main) {
        this.main = main;
    }

    public HashMap<String, Territory> terri = new HashMap<>();

    public Territory getTerritory(String name) {
        if(terri.containsKey(name))
            return terri.get(name);

        Territory territoire = new Territory(name.toLowerCase(), main);
        terri.put(name, territoire);
        return territoire;
    }

    public List<Territory> getTerritories() {
        List<Territory> result = new ArrayList<>();
        for(String section : terrDB.getDB().getConfigurationSection("").getKeys(false)) {
            result.add(getTerritory(section));
        }
        return result;
    }

    public boolean hasGuild(ActiveMob mob) {
        UUID uuid = mob.getUniqueId();
        HashMap<UUID, UUID> UUIDs = getAllBossUUID();
        return UUIDs.containsKey(uuid) && UUIDs.get(uuid) != null;
    }

    public UUID getGuildFromMob(ActiveMob mob) {
        if(!hasGuild(mob)) return null;
        HashMap<UUID, UUID> UUIDs = getAllBossUUID();
        return UUIDs.get(mob.getUniqueId());
    }

    public Territory getTerritoryFromMob(ActiveMob mob) {
        UUID mobUUID = mob.getUniqueId();
        for(String section : terrDB.getDB().getConfigurationSection("").getKeys(false)) {
            String bossUuidStr = terrDB.getDB().getString(section + ".entity.UUID");
            UUID bossUUID = (bossUuidStr != null ? UUID.fromString(bossUuidStr) : null);
            if(bossUUID != null && bossUUID.equals(mobUUID))
                return getTerritory(section.toLowerCase());
        }
        return null;
    }

    private List<Territory> getTerritoriesByGuild(UUID guildUUID) {
        List<Territory>  result = new ArrayList<>();
        for(String section : terrDB.getDB().getConfigurationSection("").getKeys(false)) {
            String guildUuidStr = terrDB.getDB().getString(section + ".owner");
            UUID uuid = (guildUuidStr != null ? UUID.fromString(guildUuidStr) : null);

            if(guildUUID.equals(uuid)) {
                result.add(getTerritory(section));
            }
        }
        return result;
    }

    public void clearAllTerritoryFromGuild(UUID guildUUID) {
        List<Territory> input = getTerritoriesByGuild(guildUUID);
        for(Territory terr : input) {
            terr.clearOwner();
        }
    }
    //UUID = Boss UUID
    //UUID = Guild UUID
    private HashMap<UUID, UUID> getAllBossUUID() {
        HashMap<UUID, UUID> result = new HashMap<>();
        for(String section : terrDB.getDB().getConfigurationSection("").getKeys(false)) {
            String bossUuidStr = terrDB.getDB().getString(section + ".entity.UUID");
            UUID bossUUID = (bossUuidStr != null ? UUID.fromString(bossUuidStr) : null);

            String guildUuidStr = terrDB.getDB().getString(section + ".owner");
            UUID guildUUID = (guildUuidStr != null ? UUID.fromString(guildUuidStr) : null);

            if(bossUUID != null)
                result.put(bossUUID, guildUUID);
        }
        return result;
    }

    public void sendTitle(String up, String down, Player player) {
        player.sendTitle(colorize(up), colorize(down), 20, 60, 20);
    }

    public Hour valueOfHour(int input) {
        switch(input) {
            case 0:
                return Hour.ZERO;
            case 1:
                return Hour.ONE;
            case 2:
                return Hour.TWO;
            case 3:
                return Hour.THREE;
            case 4:
                return Hour.FOUR;
            case 5:
                return Hour.FIVE;
            case 6:
                return Hour.SIX;
            case 7:
                return Hour.SEVEN;
            case 8:
                return Hour.EIGHT;
            case 9:
                return Hour.NINE;
            case 10:
                return Hour.TEN;
            case 11:
                return Hour.ELEVEN;
            case 12:
                return Hour.TWELVE;
            case 13:
                return Hour.THIRTEEN;
            case 14:
                return Hour.FOURTEEN;
            case 15:
                return Hour.FIFTEEN;
            case 16:
                return Hour.SIXTEEN;
            case 17:
                return Hour.SEVENTEEN;
            case 18:
                return Hour.EIGHTEEN;
            case 19:
                return Hour.NINETEEN;
            case 20:
                return Hour.TWENTY;
            case 21:
                return Hour.TWENTY_ONE;
            case 22:
                return Hour.TWENTY_TWO;
            case 23:
                return Hour.TWENTY_THREE;
            case 24:
                return Hour.TWENTY_FOUR;
        }
        return null;
    }

    public Minut valueOfMinut(int input) {
        switch (input) {
            case 0:
                return Minut.ZERO;
            case 30:
                return Minut.THIRTY;
        }
        return null;
    }
}
