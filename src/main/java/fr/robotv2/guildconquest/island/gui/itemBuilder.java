package fr.robotv2.guildconquest.island.gui;

import fr.robotv2.guildconquest.island.utilsIsland;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.utils.utilsGen;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.UUID;

public class itemBuilder {

    private ItemStack emptySlots;
    public HeadDatabaseAPI api = new HeadDatabaseAPI();

    private main main;
    public itemBuilder(main main) {
        this.main = main;
    }

    public ItemStack getEmptySlots() {
        if(emptySlots == null) {
            ItemStack deco = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta decometa = deco.getItemMeta();
            decometa.setDisplayName(utilsGen.colorize("&8"));
            deco.setItemMeta(decometa);
            emptySlots = deco;
        }
        return emptySlots;
    }

    public ItemStack getIslandSlot(String name) {
        ItemStack island = new ItemStack(Material.GRASS_BLOCK);
        NamespacedKey key = new NamespacedKey(main, "guild");
        ItemMeta islandMeta = island.getItemMeta();

        islandMeta.setDisplayName(utilsGen.colorize("&fÎle de la guilde: &e" + name));
        islandMeta.setLore(Arrays.asList("", utilsGen.colorize("&7> &fCliquez ici pour vous téléporter sur l'île"),
                utilsGen.colorize("&fde la guilde: " + name), ""));
        islandMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, name);

        island.setItemMeta(islandMeta);
        return island;
    }

    public ItemStack getCloseItem(utilsIsland.islandType type) {
        NamespacedKey key = new NamespacedKey(main, "close");

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&cRetourner au menu."));
        im.getPersistentDataContainer().set(key, PersistentDataType.STRING, type.toString());

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getNextPage(int currentPage, utilsIsland.islandType type) {
        NamespacedKey key = new NamespacedKey(main, "next-page");
        NamespacedKey island = new NamespacedKey(main, "island");

        ItemStack close = new ItemStack(Material.WHITE_WOOL);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&7Pour aller à la page suivante ->"));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, currentPage);
        im.getPersistentDataContainer().set(island, PersistentDataType.STRING, type.toString());

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getPreviousPage(int currentPage, utilsIsland.islandType type) {
        NamespacedKey key = new NamespacedKey(main, "before-page");
        NamespacedKey island = new NamespacedKey(main, "island");

        ItemStack close = new ItemStack(Material.WHITE_WOOL);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&7<- Pour aller à la page précédente"));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, currentPage);
        im.getPersistentDataContainer().set(island, PersistentDataType.STRING, type.toString());

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getIslandList(utilsIsland.islandType type) {
        NamespacedKey list = new NamespacedKey(main, "island-list");

        ItemStack close = new ItemStack(Material.BOOK);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour voir la liste des îles"));
        im.getPersistentDataContainer().set(list, PersistentDataType.STRING, type.toString());

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getTeleportToNaked(utilsIsland.islandType type) {
        NamespacedKey list = new NamespacedKey(main, "island-naked");

        ItemStack close = new ItemStack(Material.BEACON);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour vous téléporter à l'île vierge"));
        im.getPersistentDataContainer().set(list, PersistentDataType.STRING, type.toString());

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getOwnIsland(utilsIsland.islandType type) {
        NamespacedKey list = new NamespacedKey(main, "own-island");

        ItemStack close = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour vous téléporter / créer votre île de guilde."));
        im.getPersistentDataContainer().set(list, PersistentDataType.STRING, type.toString());

        close.setItemMeta(im);
        return close;
    }


    public ItemStack getBannedPlayers(UUID guildUUID) {
        NamespacedKey list = new NamespacedKey(main, "banned-players");

        ItemStack close = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour voir une liste des joueurs bannis."));
        im.getPersistentDataContainer().set(list, PersistentDataType.STRING, guildUUID.toString());

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getBannedPlayer(String name) {
        NamespacedKey banned = new NamespacedKey(main, "banned-player");

        ItemStack close = getHead(name);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fJoueur &e" + name));
        im.setLore(Arrays.asList("", utilsGen.colorize("&fClic droit pour le débannir de l'île")));
        im.getPersistentDataContainer().set(banned, PersistentDataType.STRING, name);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getComeBackItem() {
        NamespacedKey key = new NamespacedKey(main, "come-back");

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&cPour sortir de l'île."));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack addBannedPlayer() {
        NamespacedKey key = new NamespacedKey(main, "add-restricted");

        ItemStack close = new ItemStack(Material.ARROW);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&cCliquez ici pour bannir un joueur."));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getToggleRestricted(boolean isRestricted) {
        Material mat = (isRestricted ?  Material.RED_WOOL : Material.GREEN_WOOL);
        NamespacedKey key = new NamespacedKey(main, "toggle");

        ItemStack close = new ItemStack(mat);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour restreindre / autoriser l'accès à tous les membres."));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getChestItem() {
        NamespacedKey key = new NamespacedKey(main, "chest");

        ItemStack close = new ItemStack(Material.CHEST);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour ouvrir le coffre de guilde."));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getHead(String player) {
        String headID = api.addHead(CategoryEnum.HUMANS, player, Bukkit.getOfflinePlayer(player).getUniqueId());
        ItemStack playerHead = api.getItemHead(headID);
        api.removeHead(headID);
        return playerHead;
    }

    public ItemStack getSchematicMenu() {
        NamespacedKey key = new NamespacedKey(main, "schematic-menu");

        ItemStack close = new ItemStack(Material.PAPER);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour ouvrir le menu des schematics"));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getSchematicStockItem() {
        NamespacedKey key = new NamespacedKey(main, "schematic-stock");

        ItemStack close = new ItemStack(Material.CHEST);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour ouvrir le stockage"));
        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getSchematicsShop(String schemName, Double price) {
        NamespacedKey key = new NamespacedKey(main, "schematic-shop");

        ItemStack close = new ItemStack(Material.PAPER);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour acheter le schéma: &e" + schemName));
        im.setLore(Arrays.asList("", "§ePrix: " + String.valueOf(price), ""));
        im.getPersistentDataContainer().set(key, PersistentDataType.STRING, schemName);

        close.setItemMeta(im);
        return close;
    }

    public ItemStack getSchematicList(String schemName, Double number) {
        NamespacedKey key = new NamespacedKey(main, "schematic-list");

        ItemStack close = new ItemStack(Material.PAPER);
        ItemMeta im = close.getItemMeta();
        im.setDisplayName(utilsGen.colorize("&8> &fCliquez ici pour charger le schéma: &e" + schemName));
        im.setLore(Arrays.asList("", "§eVous en possèdez: " + String.valueOf(number), ""));
        im.getPersistentDataContainer().set(key, PersistentDataType.STRING, schemName);

        close.setItemMeta(im);
        return close;
    }


    public void setupEmptySlots(Inventory inv, utilsIsland.guiType type) {
        switch(type) {
            case LIST:
                for(int i=45; i<=53; i++) {
                    inv.setItem(i, getEmptySlots());
                }
                return;
            case MENU:
                for(int i=0; i<=inv.getSize() - 1; i++) {
                    inv.setItem(i, getEmptySlots());
                }
        }
    }
}
