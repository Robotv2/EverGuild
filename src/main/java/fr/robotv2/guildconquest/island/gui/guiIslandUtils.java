package fr.robotv2.guildconquest.island.gui;

import fr.robotv2.guildconquest.island.utilsIsland;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class guiIslandUtils {

    private main main;
    public guiIslandUtils(main main) {
        this.main = main;
    }

    public itemBuilder getItems() {
        return main.getUtils().getIsland().getItems();
    }

    public void openInventory(Player player, utilsIsland.islandType island) {
        Inventory inv = Bukkit.createInventory(new holders.islandHolderMenu(), 27, utilsGen.colorize("&fÎle " + island.toString().toLowerCase() + " > MENU"));
        getItems().setupEmptySlots(inv, utilsIsland.guiType.MENU);

        inv.setItem(11, getItems().getIslandList(island));
        inv.setItem(13, getItems().getTeleportToNaked(island));
        inv.setItem(15, getItems().getOwnIsland(island));

        Bukkit.getScheduler().runTask(main, () -> {
            player.openInventory(inv);
        });
    }

    public void openInventoryList(Player player, utilsIsland.islandType island, int page, String initial) {
        Inventory inv = Bukkit.createInventory(new holders.islandHolderList(), 54, utilsGen.colorize("&fÎle " + island.toString().toLowerCase() + " > LISTE"));
        getItems().setupEmptySlots(inv, utilsIsland.guiType.LIST);

        int count = 0;
        List<String> initialList = unformat(initial, page - 1);

        inv.setItem(49, getItems().getCloseItem(island));
        if(initialList.size() > 36) inv.setItem(53,  getItems().getNextPage(page, island));
        if(page >= 2) inv.setItem(45,  getItems().getPreviousPage(page, island));

        for(String name : initialList) {
            if(count >= 37) break;
            inv.setItem(count, getItems().getIslandSlot(name));
            count++;
        }

        player.openInventory(inv);
    }

    public void openBannedPlayersInventory(Player player, int page, String initial, boolean restricted) {
        Inventory inv = Bukkit.createInventory(new holders.islandHolderBannedPlayer(), 54, utilsGen.colorize("&f> Joueurs bannis"));
        getItems().setupEmptySlots(inv, utilsIsland.guiType.LIST);

        int count = 0;
        List<String> initialList = unformat(initial, page - 1);

        inv.setItem(48, getItems().getCloseItem(utilsIsland.islandType.ISLAND1));
        inv.setItem(50, getItems().addBannedPlayer());
        inv.setItem(51, getItems().getToggleRestricted(restricted));
        if(initialList.size() > 36) inv.setItem(53,  getItems().getNextPage(page, utilsIsland.islandType.ISLAND1));
        if(page >= 2) inv.setItem(45,  getItems().getPreviousPage(page, utilsIsland.islandType.ISLAND1));

        for(String name : initialList) {
            if(count >= 37) break;
            inv.setItem(count, getItems().getBannedPlayer(name));
            count++;
        }

        player.openInventory(inv);
    }

    public void openSchemShop(Player player, int page, String initial) {
        Inventory inv = Bukkit.createInventory(new holders.islandHolderSchemShop(), 54, utilsGen.colorize("&f> Schematics shop"));
        getItems().setupEmptySlots(inv, utilsIsland.guiType.LIST);

        int count = 0;
        List<String> initialList = unformat(initial, page - 1);

        inv.setItem(48, getItems().getCloseItem(utilsIsland.islandType.ISLAND1));
        inv.setItem(50, getItems().getSchematicStockItem());
        if(initialList.size() > 36) inv.setItem(53,  getItems().getNextPage(page, utilsIsland.islandType.ISLAND1));
        if(page >= 2) inv.setItem(45,  getItems().getPreviousPage(page, utilsIsland.islandType.ISLAND1));

        for(String name : initialList) {
            if(count >= 37) break;
            String[] schem = name.split("-");
            inv.setItem(count, getItems().getSchematicsShop(schem[0], Double.parseDouble(schem[1])));
            count++;
        }

        player.openInventory(inv);
    }

    public void openSchemList(Player player, String initial) {
        Inventory inv = Bukkit.createInventory(new holders.islandHolderSchemList(), 54, utilsGen.colorize("&f> Schematics shop"));
        getItems().setupEmptySlots(inv, utilsIsland.guiType.LIST);

        int count = 0;
        List<String> initialList = unformat(initial, 0);

        inv.setItem(49, getItems().getCloseItem(utilsIsland.islandType.ISLAND1));

        for(String name : initialList) {
            if(count >= 37) break;
            String[] schem = name.split("-");
            inv.setItem(count, getItems().getSchematicList(schem[0], Double.parseDouble(schem[1])));
            count++;
        }
        player.openInventory(inv);
    }

    public List<String> unformat(String initial, int page) {
        List<String> result = new ArrayList<>();
        if(initial == null || initial.isEmpty()) return result;

        String[] list = initial.split(";");
        Collections.addAll(result, list);
        Collections.sort(result);

        if(result.size() < 36) return result;

        for(int i=0; i<=36*page; i++) {
            result.remove(i);
        }
        return result;
    }
}
