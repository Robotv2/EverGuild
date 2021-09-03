package fr.robotv2.guildconquest.island;

import fr.robotv2.guildconquest.island.gui.holders;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class islandListeners implements Listener {

    private main main;
    public islandListeners(main main) {
        this.main = main;
    }

    public List<UUID> hasNpc = new ArrayList<>();
    public HashMap<UUID, Location> npcLoc = new HashMap<>();

    public HashMap<UUID, Integer> taskID = new HashMap<>();

    @EventHandler
    public void onEnter(RegionEnteredEvent e) {
        String region = e.getRegionName();
        Player player = e.getPlayer();
        if(!region.startsWith("island-")) return;

        e.setCancelled(true);
        String island = region.replace("island-", "");
        main.getUtils().getIsland().getGui().openInventory(player, utilsIsland.islandType.valueOf(island.toUpperCase()));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        String worldName = player.getWorld().getName();
        if(!worldName.startsWith("island-")) return;

        Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
        if(guild == null || !worldName.endsWith(guild.getUuid().toString())) {
            e.setCancelled(true);
            player.sendMessage(utilsGen.colorize("&cVous ne pouvez pas faire ça ici."));
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        String worldName = player.getWorld().getName();
        if(!worldName.startsWith("island-")) return;

        Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
        if(guild == null || !worldName.endsWith(guild.getUuid().toString())) {
            e.setCancelled(true);
            player.sendMessage(utilsGen.colorize("&cVous ne pouvez pas faire ça ici."));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager().getWorld().getName().startsWith("island-") && e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
            if(guild == null || !player.getWorld().getName().endsWith(guild.getUuid().toString())) {
                e.setCancelled(true);
                player.sendMessage(utilsGen.colorize("&cVous ne pouvez pas faire ça ici."));
            }
        }
    }

    @EventHandler
    public void npcClick(NPCRightClickEvent e) {
        Player player = e.getClicker();
        NPC npc = e.getNPC();
        Guild guild = main.getUtils().getUtilsGuild().getGuild(player);

        if(!npc.data().has("guild")) return;
        if(guild == null || !npc.data().get("guild").equals(guild.getUuid().toString())) {
            main.getUtils().getIsland().teleportBack(player);
            return;
        }

        if(main.getUtils().getUtilsGuild().isChef(guild, player) && player.isSneaking()) {
            hasNpc.add(player.getUniqueId());
            npcLoc.put(player.getUniqueId(), npc.getStoredLocation());

            npc.destroy();
            startNpcReset(player.getUniqueId());
            player.sendMessage(utilsGen.colorize("&7Vous venez de récupérer le pnj. Appuyer sur shift n'importe où pour le re-déposer."));
            return;
        }
        main.getUtils().getIsland().openIslandGuildMenu(player);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if(hasNpc.contains(player.getUniqueId()) && !player.isSneaking()) {
            hasNpc.remove(player.getUniqueId());
            npcLoc.remove(player.getUniqueId());
            main.getUtils().getIsland().createNpc(player.getLocation());
            player.sendMessage(utilsGen.colorize("&7Vous venez de poser le PNJ."));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        ItemStack it = e.getCurrentItem();
        Player player = (Player) e.getWhoClicked();
        InventoryHolder holder = e.getInventory().getHolder();
        if(it == null) return;

        if(holder instanceof holders.islandHolderList) {
            e.setCancelled(true);

            NamespacedKey guild = new NamespacedKey(main, "guild");
            NamespacedKey island = new NamespacedKey(main, "island");
            NamespacedKey next_page = new NamespacedKey(main, "next-page");
            NamespacedKey before_page = new NamespacedKey(main, "before-page");
            NamespacedKey close = new NamespacedKey(main, "close");

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            if(data.has(guild, PersistentDataType.STRING)) {
                String name = data.get(guild, PersistentDataType.STRING);
                main.getUtils().getIsland().visitGuildIsland(player, name);
                player.closeInventory();
            }

            else if(data.has(next_page, PersistentDataType.INTEGER)) {
                int currentPage = data.get(next_page, PersistentDataType.INTEGER);
                utilsIsland.islandType type = utilsIsland.islandType.valueOf(data.get(island, PersistentDataType.STRING));
                main.getUtils().getIsland().openInventoryList(player, type, currentPage + 1);
            }

            else if(data.has(before_page, PersistentDataType.INTEGER)) {
                int currentPage = data.get(before_page, PersistentDataType.INTEGER);
                utilsIsland.islandType type = utilsIsland.islandType.valueOf(data.get(island, PersistentDataType.STRING));
                main.getUtils().getIsland().openInventoryList(player, type, currentPage - 1);
            }

            else if(data.has(close, PersistentDataType.STRING)) {
                utilsIsland.islandType type = utilsIsland.islandType.valueOf(data.get(close, PersistentDataType.STRING));
                main.getUtils().getIsland().getGui().openInventory(player, type);
            }
        }
        else if(holder instanceof holders.islandHolderMenu) {
            e.setCancelled(true);

            NamespacedKey own = new NamespacedKey(main, "own-island");
            NamespacedKey naked = new NamespacedKey(main, "island-naked");
            NamespacedKey list = new NamespacedKey(main, "island-list");

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            if(data.has(list, PersistentDataType.STRING)) {
                utilsIsland.islandType type = utilsIsland.islandType.valueOf(data.get(list, PersistentDataType.STRING));
                main.getUtils().getIsland().openInventoryList(player, type, 1);
            }
            else if(data.has(naked, PersistentDataType.STRING)) {
                utilsIsland.islandType type = utilsIsland.islandType.valueOf(data.get(naked, PersistentDataType.STRING));
                main.getUtils().getIsland().visitNakedIsland(player, type);
                player.closeInventory();
            }
            else if(data.has(own, PersistentDataType.STRING)) {
                utilsIsland.islandType type = utilsIsland.islandType.valueOf(data.get(own, PersistentDataType.STRING));
                main.getUtils().getIsland().teleportOrCreate(player, type);
                player.closeInventory();
            }
        }
        else if(holder instanceof holders.islandHolderGuildMenu) {
            e.setCancelled(true);

            NamespacedKey banned_players = new NamespacedKey(main, "banned-players");
            NamespacedKey come_back = new NamespacedKey(main, "come-back");
            NamespacedKey chest = new NamespacedKey(main, "chest");
            NamespacedKey schem = new NamespacedKey(main, "schematic-menu");

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            if(data.has(banned_players, PersistentDataType.STRING)) {
                UUID guildUUID = UUID.fromString(data.get(banned_players, PersistentDataType.STRING));
                main.getUtils().getIsland().openBannedPlayersInventory(player, guildUUID, 1);
            }
            else if(data.has(come_back, PersistentDataType.INTEGER)) {
                main.getUtils().getIsland().teleportBack(player);
            }
            else if(data.has(chest, PersistentDataType.INTEGER)) {
                main.getUtils().getIsland().openGuildChest(player);
            }
            else if(data.has(schem, PersistentDataType.INTEGER)) {
                Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
                main.getUtils().getIsland().openSchemShop(player, guild.getUuid(), 1);
            }
        }
        else if(holder instanceof holders.islandHolderBannedPlayer) {
            e.setCancelled(true);

            NamespacedKey close = new NamespacedKey(main, "close");
            NamespacedKey banned = new NamespacedKey(main, "banned-player");
            NamespacedKey add_banned = new NamespacedKey(main, "add-restricted");
            NamespacedKey toggle = new NamespacedKey(main, "toggle");

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            if(data.has(close, PersistentDataType.STRING)) {
                main.getUtils().getIsland().openIslandGuildMenu(player);
            }
            else if(data.has(banned, PersistentDataType.STRING)) {
                String name = data.get(banned, PersistentDataType.STRING);
                UUID guildUUID = main.getUtils().getUtilsGuild().getGuild(player).getUuid();
                main.getUtils().getIsland().removeRestrictedPlayer(name, guildUUID);
                main.getUtils().getIsland().openBannedPlayersInventory(player, guildUUID, 1);
            }
            else if(data.has(add_banned, PersistentDataType.INTEGER)) {
                main.getUtils().getIsland().banned_player.remove(player.getUniqueId());
                main.getUtils().getIsland().banned_player.add(player.getUniqueId());
                player.sendMessage(utilsGen.colorize("&7écrivez le pseudonyme, de la personne que vous souhaitez bannir, dans le chat."));
                player.closeInventory();
            }
            else if(data.has(toggle, PersistentDataType.INTEGER)) {
                Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
                main.getUtils().getIsland().toggleRestriction(player, guild.getUuid());
                main.getUtils().getIsland().openBannedPlayersInventory(player, guild.getUuid(), 1);
                player.closeInventory();
            }
        }
        else if(holder instanceof holders.islandHolderSchemShop) {
            e.setCancelled(true);

            NamespacedKey close = new NamespacedKey(main, "close");
            NamespacedKey schem = new NamespacedKey(main, "schematic-shop");
            NamespacedKey stock = new NamespacedKey(main, "schematic-stock");

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            if(data.has(close, PersistentDataType.STRING)) {
                main.getUtils().getIsland().openIslandGuildMenu(player);
            }
            else if(data.has(schem, PersistentDataType.STRING)) {
                String schemStr = data.get(schem, PersistentDataType.STRING);
                main.getUtils().getIsland().buySchematics(player, schemStr);
            }
            else if(data.has(stock, PersistentDataType.INTEGER)) {
                Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
                main.getUtils().getIsland().openSchemList(player, guild.getUuid());
            }
        }
        else if(holder instanceof holders.islandHolderSchemList) {
            e.setCancelled(true);

            NamespacedKey close = new NamespacedKey(main, "close");
            NamespacedKey list = new NamespacedKey(main, "schematic-list");

            ItemMeta itMeta = it.getItemMeta();
            PersistentDataContainer data = itMeta.getPersistentDataContainer();

            if(data.has(close, PersistentDataType.STRING)) {
                Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
                main.getUtils().getIsland().openSchemShop(player, guild.getUuid(), 1);
            }
            else if(data.has(list, PersistentDataType.STRING)) {
                String schem = data.get(list, PersistentDataType.STRING);
                main.getUtils().getIsland().getSchematics().initSchem(player, schem);
                main.getUtils().getIsland().getSchematics().loadSchem(player);
                player.sendMessage(utilsGen.colorize("&7Vous venez de charger le schématique: " + schem));
                player.closeInventory();
            }
        }
    }

    public void startNpcReset(UUID playerUUID) {
        if(taskID.containsKey(playerUUID)) {
            Bukkit.getScheduler().cancelTask(taskID.get(playerUUID));
        }
        BukkitTask task = Bukkit.getScheduler().runTaskLater(main, () -> {
            Player player = Bukkit.getPlayer(playerUUID);
            if(player == null || !player.isOnline() || hasNpc.contains(playerUUID)) {
                Location npcLocation = npcLoc.get(playerUUID);
                main.getUtils().getIsland().createNpc(npcLocation);

                hasNpc.remove(playerUUID);
                npcLoc.remove(playerUUID);
                taskID.remove(playerUUID);

                if(player != null && player.isOnline())
                    player.sendMessage(utilsGen.colorize("&cVous avez pris trop de temps pour placer le pnj."));
            }
        }, 400L);
        taskID.put(playerUUID, task.getTaskId());
    }
}
