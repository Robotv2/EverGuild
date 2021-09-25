package fr.robotv2.guildconquest.territory.ui;

import fr.robotv2.guildconquest.main;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static fr.robotv2.guildconquest.utils.utilsGen.colorize;

public class ItemAPI {

    private static main main;
    public ItemAPI(main main) {
        ItemAPI.main = main;
    }

    public static HeadDatabaseAPI headAPI = new HeadDatabaseAPI();

    public static ItemStack getHead(String player) {
        String headID = headAPI.addHead(CategoryEnum.HUMANS, player, Bukkit.getOfflinePlayer(player).getUniqueId());
        ItemStack playerHead = headAPI.getItemHead(headID);
        headAPI.removeHead(headID);
        return playerHead;
    }

    public static ItemStack getHead(int number) {
        return headAPI.getItemHead(String.valueOf(number));
    }

    public static class itemBuilder {
        private Material type;
        private int amount;
        private ItemMeta meta = new ItemStack(Material.GRASS).getItemMeta();

        public itemBuilder setType(Material type) {
            this.type = type;
            return this;
        }

        public itemBuilder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public itemBuilder setName(String name) {
            this.meta.setDisplayName(colorize(name));
            return this;
        }

        public itemBuilder setLore(String... lore) {
            List<String> result = new ArrayList<>(List.of(lore));
            for(int i = 0; i < result.size(); i++) {
                String line = result.get(i);
                result.set(i, colorize(line));
            }
            this.meta.setLore(result);
            return this;
        }

        public itemBuilder setLore(List<String> lore) {
            for(int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                lore.set(i, colorize(line));
            }
            this.meta.setLore(lore);
            return this;
        }

        public itemBuilder setKey(String keyStr, String value) {
            NamespacedKey key = new NamespacedKey(main, keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
            return this;
        }

        public itemBuilder setKey(String keyStr, double value) {
            NamespacedKey key = new NamespacedKey(main, keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
            return this;
        }

        public itemBuilder setKey(String keyStr, int value) {
            NamespacedKey key = new NamespacedKey(main, keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
            return this;
        }

        public itemBuilder setKey(String keyStr, float value) {
            NamespacedKey key = new NamespacedKey(main, keyStr);
            this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
            return this;
        }

        public itemBuilder addEnchant(Enchantment enchant, int level, boolean ignoreLevelRestriction) {
            this.meta.addEnchant(enchant, level, ignoreLevelRestriction);
            return this;
        }

        public itemBuilder setUnbreakable(boolean unbreakable) {
            this.meta.setUnbreakable(unbreakable);
            return this;
        }

        public itemBuilder addFlags(ItemFlag... flags) {
            this.meta.addItemFlags(flags);
            return this;
        }

        public ItemStack build() {
            if(this.type == null)
                type = Material.GRASS;
            if(this.amount <= 0)
                amount = 1;
            ItemStack item = new ItemStack(type, amount);
            item.setItemMeta(meta);
            return item;
        }
    }
}
