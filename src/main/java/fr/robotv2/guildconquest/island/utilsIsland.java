package fr.robotv2.guildconquest.island;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.robotv2.guildconquest.island.gui.guiIslandUtils;
import fr.robotv2.guildconquest.island.gui.holders;
import fr.robotv2.guildconquest.island.gui.itemBuilder;
import fr.robotv2.guildconquest.island.schematics.utilsSchem;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Guild;
import fr.robotv2.guildconquest.utils.utilsGen;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class utilsIsland {

    private main main;
    private itemBuilder itemBuilder;
    private guiIslandUtils guiIslandUtils;
    private utilsSchem utilsSchem;
    public utilsIsland(main main) {
        this.main = main;
        this.itemBuilder = new itemBuilder(main);
        this.guiIslandUtils = new guiIslandUtils(main);
        this.utilsSchem = new utilsSchem(main);
    }

    public List<UUID> banned_player = new ArrayList<>();

    public enum guiType {
        MENU, LIST;
    }

    public enum islandType {
        ISLAND1, ISLAND2, ISLAND3;
    }

    public itemBuilder getItems() { return itemBuilder; }

    public guiIslandUtils getGui() { return guiIslandUtils; }

    public utilsSchem getSchematics() { return utilsSchem; }

    public void createWorld(World originalWorld, String newWorldName) {
        main.multiverseAPI.getMVWorldManager().cloneWorld(originalWorld.getName(), newWorldName);
        main.multiverseAPI.getMVWorldManager().loadWorld(newWorldName);

        World current = Bukkit.getWorld(newWorldName);
        if (current != null) {
            current.setTime(1000L);
            current.setStorm(false);
            current.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            current.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        }
        main.sendDebug("&fCopie du monde &e" + originalWorld.getName() + " &fvers le monde &e" + newWorldName);
    }

    public void deleteWorld(UUID guildUUID) {
        World world = Bukkit.getWorld("island-" + guildUUID.toString());
        if(world != null) {
            world.getPlayers().forEach(this::teleportBack);
            for(Entity ent : world.getEntities()) {
                if(ent instanceof NPC) {
                    NPC npc = (NPC) ent;
                    npc.destroy();
                }
            }
            main.multiverseAPI.getMVWorldManager().unloadWorld(world.getName());
            main.multiverseAPI.getMVWorldManager().deleteWorld(world.getName(), true, true);
        }
    }

    public void openInventoryList(Player player, utilsIsland.islandType type, int page) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("open-inventory-list");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(type.toString());
        out.writeInt(page);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void openBannedPlayersInventory(Player player, UUID guildUUID, int page) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("open-banned-list");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(guildUUID.toString());
        out.writeInt(page);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void openSchemShop(Player player, UUID guildUUID, int page) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("open-schem-shop");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(guildUUID.toString());
        out.writeInt(page);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void openSchemList(Player player, UUID guildUUID) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("open-schem-stock");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(guildUUID.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void openIslandGuildMenu(Player player) {
        Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
        Inventory inv = Bukkit.createInventory(new holders.islandHolderGuildMenu(), 27, utilsGen.colorize("&fÎle " + guild.getName() + " > MENU"));
        getItems().setupEmptySlots(inv, guiType.MENU);

        inv.setItem(0, getItems().getComeBackItem());
        inv.setItem(11, getItems().getBannedPlayers(guild.getUuid()));
        inv.setItem(13, getItems().getChestItem());
        inv.setItem(15, getItems().getSchematicMenu());

        player.openInventory(inv);
    }

    public void teleportBack(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("teleport-back");
        out.writeUTF(player.getUniqueId().toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void visitNakedIsland(Player player, islandType type) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("teleport-naked-island");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(type.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void visitGuildIsland(Player player, String guildName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("teleport-guild-island");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(guildName);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void teleportOrCreate(Player player, islandType type) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("teleport-or-create");
        out.writeUTF(player.getUniqueId().toString());
        out.writeUTF(type.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void addRestrictedPlayer(String playerName, UUID guildUUID, Player sender) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("add-restricted-player");
        out.writeUTF(guildUUID.toString());
        out.writeUTF(sender.getUniqueId().toString());
        out.writeUTF(playerName);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
        kickPlayer(playerName, guildUUID);
    }

    public void removeRestrictedPlayer(String playerName, UUID guildUUID) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("remove-restricted-player");
        out.writeUTF(guildUUID.toString());
        out.writeUTF(playerName);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void toggleRestriction(Player sender, UUID guildUUID) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("toggle-restriction");
        out.writeUTF(sender.getUniqueId().toString());
        out.writeUTF(guildUUID.toString());

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void buySchematics(Player sender, String schem) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("buy-schematics");
        out.writeUTF(sender.getUniqueId().toString());
        out.writeUTF(schem);

        main.getLast().sendPluginMessage(main, main.channel, out.toByteArray());
    }

    public void createNpc(Location loc) {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, utilsGen.colorize("&e&lVOYAGEUR"));
        String guildUUID = loc.getWorld().getName().replace("island-", "");
        npc.data().setPersistent("guild", guildUUID);
        npc.setProtected(true);
        npc.spawn(loc, SpawnReason.CREATE);
    }

    public void initializeNPC(UUID guildUUID, String npcLocationStr) {
        String[] list = npcLocationStr.split(";");
        if(list.length != 6) return;

        Double X = Double.valueOf(list[0]);
        Double Y = Double.valueOf(list[1]);
        Double Z = Double.valueOf(list[2]);
        Float yaw = Float.valueOf(list[3]);
        Float pitch = Float.valueOf(list[4]);
        String world = list[5];

        Location npcLocation = new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
        createNpc(npcLocation);
    }

    public void initializeChest(UUID guildUUID) {
        World world = Bukkit.getWorld("island-" + guildUUID.toString());
        if(world == null) return;

        org.bukkit.block.data.type.Chest chest_data_right;
        org.bukkit.block.data.type.Chest chest_data_left;

        Location air1 = new Location(world,201, 2, 200);
        Location air2 = new Location(world, 200, 2, 200);
        world.getBlockAt(air1).setType(Material.AIR);
        world.getBlockAt(air2).setType(Material.AIR);

        Location loc1 = new Location(world, 201, 1, 200);
        Location loc2 = new Location(world, 200, 1, 200);

        Block block_right = world.getBlockAt(loc1);
        Block block_left = world.getBlockAt(loc2);

        block_right.setType(Material.CHEST);
        block_left.setType(Material.CHEST);

        Chest block_state_right = (Chest) block_right.getState();
        chest_data_right = (org.bukkit.block.data.type.Chest) block_state_right.getBlockData();
        chest_data_right.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
        block_right.setBlockData(chest_data_right, true);

        Chest block_state_left = (Chest) block_left.getState();
        chest_data_left = (org.bukkit.block.data.type.Chest) block_state_left.getBlockData();
        chest_data_left.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
        block_left.setBlockData(chest_data_left, true);
    }

    public void openGuildChest(Player player) {
        Location chestLocation = new Location(player.getLocation().getWorld(), 201, 1, 200);
        Block block = chestLocation.getBlock();

        if(block.getType() != Material.CHEST) return;

        Chest chest = (Chest) block.getState();
        player.openInventory(chest.getInventory());
    }

    public void kickPlayer(String playerName, UUID guildUUID) {
        World world = Bukkit.getWorld("island-" + guildUUID.toString());
        Player player = Bukkit.getPlayer(playerName);

        if(player == null) return;
        if(world == null) return;
        if(!player.getWorld().equals(world)) return;

        teleportBack(player);
        player.sendMessage(utilsGen.colorize("&cL'accès à l'île vous a été restreint."));
    }

    public boolean isInGuildIsland(Player player) {
        Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
        return player.getWorld().getName().endsWith(guild.getUuid().toString());
    }
}
