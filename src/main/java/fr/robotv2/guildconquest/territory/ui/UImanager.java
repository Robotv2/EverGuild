package fr.robotv2.guildconquest.territory.ui;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Territory;
import fr.robotv2.guildconquest.territory.Hour;
import fr.robotv2.guildconquest.territory.Minut;
import fr.robotv2.guildconquest.territory.utilsTerritory;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.royawesome.jlibnoise.module.combiner.Min;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

import static fr.robotv2.guildconquest.territory.fastboard.FastReflection.OBC_PACKAGE;
import static fr.robotv2.guildconquest.utils.utilsGen.colorize;

public class UImanager implements Listener {

    private main main;
    private utilsTerritory utils;
    public UImanager(main main) {
        this.main = main;
        this.utils = main.getTerritory().getUtil();
    }

    private ItemStack emptySlots;
    private long MINUT = 60000L;
    private long HOUR = 60 * MINUT;
    private long DAY = 24 * HOUR;

    public ItemStack getEmptySlots() {
        if(emptySlots == null) {
            this.emptySlots = new ItemAPI.itemBuilder()
                    .setType(Material.BLACK_STAINED_GLASS_PANE)
                    .setAmount(0)
                    .setName("&8")
                    .setLore(new ArrayList<>())
                    .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                    .build();
        }
        return emptySlots;
    }

    public ItemStack getItemTime(Territory territoire, Hour hour, Minut minut) {
        if(hour == null || minut == null) {
            hour = utils.valueOfHour(territoire.getPossibleHour());
            minut = utils.valueOfMinut(territoire.getPossibleMinut());
            if(hour == null || minut == null) {
                hour = Hour.TWENTY_ONE;
                minut = Minut.ZERO;
            }
        }

        ItemAPI.itemBuilder builder = new ItemAPI.itemBuilder();
        builder.setAmount(1);
        builder.setName("&7Pour changer l'heure à &e" + hour.getNumber() + " &7heures(s) et " + minut.getNumber() + " minute(s)");

        builder.setType(Material.GREEN_STAINED_GLASS_PANE);
        builder.setKey("HOUR", hour.getNumber());
        builder.setKey("MINUT", minut.getNumber());
        builder.setKey("territory", territoire.getRegion());

        if(hour.equals(utils.valueOfHour(territoire.getPossibleHour())) && minut.equals(utils.valueOfMinut(territoire.getPossibleMinut()))) {
            builder.addEnchant(Enchantment.ARROW_FIRE, 1, true);
            builder.addFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        }
        return builder.build();
    }

    public HashMap<Player, Hour> playerHour = new HashMap<>();
    public HashMap<Player, Minut> playerMinut = new HashMap<>();

    public void open(org.bukkit.entity.Player player, Territory territoire) {
        Inventory inv = Bukkit.createInventory(new holders.territoryChange(), 27, "boss1");
        for(int i=0; i<=inv.getSize() - 1; i++) {
            inv.setItem(i, getEmptySlots());
        }

        if(territoire.getPossibleHour() != null) {
            playerHour.put(player, utils.valueOfHour(territoire.getPossibleHour()));
        } else {
            playerHour.put(player, Hour.TWENTY_ONE);
        }

        if(territoire.getPossibleMinut() != null) {
            playerMinut.put(player, utils.valueOfMinut(territoire.getPossibleMinut()));
        } else {
            playerMinut.put(player, Minut.ZERO);
        }

        inv.setItem(11,ItemAPI.getHead(8790));
        inv.setItem(13, getItemTime(territoire, playerHour.get(player), playerMinut.get(player)));
        inv.setItem(15, ItemAPI.getHead(8787));

        player.openInventory(inv);
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ItemStack it = e.getCurrentItem();
        org.bukkit.entity.Player player = (Player) e.getWhoClicked();

        if(it == null) return;

        if(e.getInventory().getHolder() instanceof holders.territoryChange) {
            e.setCancelled(true);
            int slot = e.getRawSlot();

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            NamespacedKey hour = new NamespacedKey(main, "HOUR");
            NamespacedKey minut = new NamespacedKey(main, "MINUT");
            NamespacedKey region = new NamespacedKey(main, "territory");

            Territory territoire = utils.getTerritory(e.getView().getTitle());
            if(territoire == null) return;

            if (data.has(region, PersistentDataType.STRING)) {
                Hour currentHour = utils.valueOfHour(data.get(hour, PersistentDataType.INTEGER));
                Minut currentMinut = utils.valueOfMinut(data.get(minut, PersistentDataType.INTEGER));

                territoire.setStartTime(currentHour, currentMinut);
                player.closeInventory();
                player.sendMessage(colorize(main.prefix + "&7Vous venez de mettre l'heure du début du koth à &e&l"
                            + currentHour.getNumber() + "H"
                            + currentMinut.getNumber() + "M" + " &7le lendemain de la prise de la zone."));

            } else if(slot == 11) {
                toggle(clickType.REMOVE, player);
                actualizeItem(territoire, e.getInventory(), player);
            } else if(slot == 15) {
                toggle(clickType.ADD, player);
                actualizeItem(territoire, e.getInventory(), player);
            }
        }
    }

    public void toggle(clickType type, Player player) {
        Hour hour = playerHour.get(player);
        Minut minut = playerMinut.get(player);
        switch (type) {
            case ADD:
                if(hour == Hour.TWENTY_THREE && minut == Minut.THIRTY)
                    break;
                if(minut == Minut.ZERO)
                    playerMinut.put(player, Minut.THIRTY);
                else {
                    playerMinut.put(player, Minut.ZERO);
                    playerHour.put(player, utils.valueOfHour(hour.getNumber() + 1));
                }
                break;
            case REMOVE:
                if(hour == Hour.ZERO && minut == Minut.THIRTY)
                    break;
                if(minut == Minut.THIRTY)
                    playerMinut.put(player, Minut.ZERO);
                else {
                    playerMinut.put(player, Minut.THIRTY);
                    playerHour.put(player, utils.valueOfHour(hour.getNumber() - 1));
                }
                break;
        }
    }

    public void actualizeItem(Territory territoire, Inventory inv, Player player) {
        inv.setItem(13, getItemTime(territoire, playerHour.get(player), playerMinut.get(player)));
    }

    public enum clickType {
        ADD, REMOVE;
    }
}

