package fr.robotv2.guildconquest.object;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import fr.robotv2.guildconquest.configs.terrDB;
import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.territory.Minut;
import fr.robotv2.guildconquest.territory.fastboard.FastBoard;
import fr.robotv2.guildconquest.territory.Hour;
import fr.robotv2.guildconquest.territory.comparator.highToLow;
import fr.robotv2.guildconquest.territory.utilsTerritory;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractWorld;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static fr.robotv2.guildconquest.territory.territoryManager.boards;
import static fr.robotv2.guildconquest.utils.utilsGen.colorize;

public class Territory {

    private final main main;

    private final BukkitAPIHelper helper;
    private final String type;
    private final utilsTerritory utils;

    private final ProtectedRegion WORLDGUARD_REGION;
    private final String region;
    private final String display;
    private final Location location;

    public List<org.bukkit.entity.Player> players = new ArrayList<>();

    public Territory(String region, main main) {
        this.region = region.toLowerCase();
        this.display = colorize(terrDB.getDB().getString(getRegion() + ".display"));
        this.location = setupBossSpawnLocation();
        this.WORLDGUARD_REGION = WorldGuard.getInstance().getPlatform().getRegionContainer()
                .get(BukkitAdapter.adapt(getSpawnLocation().getWorld()))
                .getRegion("territory-" + region);

        this.main = main;
        this.helper = MythicMobs.inst().getAPIHelper();
        this.type = terrDB.getDB().getString(getRegion() + ".entity.type");

        utils = main.getTerritory().getUtil();
    }

    public void init() {
        bar = Bukkit.createBossBar("Initialisation de la bossbar", BarColor.GREEN, BarStyle.SOLID);
        bar.setVisible(false);
        if(hasOwner())
            setConquerBar(main.getUtils().getUtilsGuild().getGuild(getOwner()));
        else if(isWeak())
            setWeakBar();

        spawnNpc();
        if(!isWeak())
            spawnBoss();
        if(isWeak() && getPossibleEnd() > Instant.now().toEpochMilli()) {
            initTimer();
        }
    }

    public String getRegion() {
        return region;
    }

    public String getDisplay() { return display; }

    public List<org.bukkit.entity.Player> getPlayers() {
        players.removeIf(player -> !player.isOnline());
        return players;
    }

    private Location setupBossSpawnLocation() {
        FileConfiguration config = terrDB.getDB();

        double X = config.getDouble(getRegion() + ".entity.spawn.X");
        double Y = config.getDouble(getRegion() + ".entity.spawn.Y");
        double Z = config.getDouble(getRegion() + ".entity.spawn.Z");
        float yaw = (float) config.getDouble(getRegion() + ".entity.spawn.YAW");
        float pitch = (float) config.getDouble(getRegion() + ".entity.spawn.PITCH");
        World world = Bukkit.getWorld(config.getString(getRegion() + ".entity.spawn.WORLD"));

        return new Location(world, X, Y, Z, yaw, pitch);
    }

    /////////
    //OWNER//
    /////////

    public UUID getOwner() {
        String uuidStr = terrDB.getDB().getString(getRegion() + ".owner");
        if(uuidStr != null) {
            return UUID.fromString(uuidStr);
        }
        return null;
    }

    public boolean hasOwner() {
        return terrDB.getDB().getString(getRegion() + ".owner") != null;
    }

    public void setOwner(UUID guildUUID) {
        terrDB.getDB().set(getRegion() + ".owner", guildUUID.toString());
        terrDB.saveDB();
    }

    public void clearOwner() {
        terrDB.getDB().set(getRegion() + ".owner", null);
        terrDB.saveDB();
    }

    /////////
    ///BOSS//
    /////////

    public Location getSpawnLocation() {
        return location;
    }
    public String getType() {
        return type;
    }
    public BukkitTask task_boss;

    public UUID getBossUUID() {
        String uuidStr = terrDB.getDB().getString(getRegion() + ".entity.UUID");
        if(uuidStr != null) {
            return UUID.fromString(uuidStr);
        }
        return null;
    }

    public void setBossUUID(UUID uuid) {
        terrDB.getDB().set(getRegion() + ".entity.UUID", uuid.toString());
        terrDB.saveDB();
    }

    public void spawnBoss() {
        MythicMob mob = helper.getMythicMob(getType());
        AbstractWorld world = io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter.adapt(getSpawnLocation()
                .getWorld());
        AbstractLocation spawnLocation = new AbstractLocation(world
                , getSpawnLocation().getX()
                , getSpawnLocation().getY()
                , getSpawnLocation().getZ()
                , getSpawnLocation().getYaw()
                , getSpawnLocation().getPitch());
        ActiveMob boss = mob.spawn(spawnLocation, 1);
        boss.setups();
        boss.getEntity().setAI(false);

        setBossUUID(boss.getUniqueId());
        Objects.requireNonNull(Bukkit.getEntity(getBossUUID())).setPersistent(true);
        startAutoReloc(boss);
    }

    public void startAutoReloc(ActiveMob mob) {
        if(task_boss != null)
            task_boss.cancel();

        task_boss = main.getServer().getScheduler().runTaskTimer(main, () -> {
            Entity boss = Bukkit.getEntity(getBossUUID());
            if(boss == null || !boss.isValid()) {
                task_boss.cancel();
                spawnBoss();
                return;
            }
            Location bossLoc = boss.getLocation();
            if(!WORLDGUARD_REGION.contains(bossLoc.getBlockX(), bossLoc.getBlockY(), bossLoc.getBlockZ())) {
                boss.remove();
                spawnBoss();
            }
        }, 0, 20);
    }

    public boolean isBossSpawned() {
        return task_boss != null;
    }

    public BukkitTask getBossTask() {
        return task_boss;
    }

    ////////
    //WEAK//
    ////////

    public void clearWeakTime() {
        terrDB.getDB().set(getRegion() + ".weak-time", null);
    }

    public Long getWeakTime() {
        return terrDB.getDB().getLong(getRegion() + ".weak-time");
    }

    public void setWeakTime() {
        terrDB.getDB().set(getRegion() + ".weak-time", Instant.now().toEpochMilli());
        terrDB.saveDB();
    }

    public boolean isWeak() {
        Object obj = terrDB.getDB().get(getRegion() + ".weak");
        if(obj == null)
            return false;
        else
            return terrDB.getDB().getBoolean(getRegion() + ".weak");
    }

    private void setWeak(boolean value) {
        terrDB.getDB().set(getRegion() + ".weak", value);
        terrDB.saveDB();
    }

    public void startWeakening() {
        if(isWeak()) return;
        setWeak(true);
        setWeakTime();
        setWeakBar();

        LocalDateTime start = Instant.ofEpochSecond(getWeakTime()).atZone(ZoneId.of("Europe/Paris")).toLocalDateTime();
        System.out.println(start);

        int HOUR = getPossibleHour();
        System.out.println(HOUR);
        int HOUR_NEW = HOUR - start.getHour();
        System.out.println(HOUR_NEW);
        int MINUT = getPossibleMinut();
        System.out.println(MINUT);
        int MINUT_NEW = MINUT - start.getMinute();
        System.out.println(MINUT_NEW);

        long RESULT = getWeakTime() + DAY + (this.MINUT * MINUT_NEW) + (this.HOUR * HOUR_NEW);
        System.out.println(RESULT);

        setPossibleEnd(RESULT);
        initTimer();
    }

    public void endWeakening() {
        if(!isWeak()) return;
        clearOwner();
        clearWeakTime();
        deleteHologram();
    }

    ///////
    //NPC//
    ///////

    public Location getSpawnNpcLocation() {
        FileConfiguration config = terrDB.getDB();

        double X = config.getDouble(getRegion() + ".NPC.X");
        double Y = config.getDouble(getRegion() + ".NPC.Y");
        double Z = config.getDouble(getRegion() + ".NPC.Z");
        float yaw = (float) config.getDouble(getRegion() + ".NPC.YAW");
        float pitch = (float) config.getDouble(getRegion() + ".NPC.PITCH");
        World world = Bukkit.getWorld(config.getString(getRegion() + ".NPC.WORLD"));

        return new Location(world, X, Y, Z, yaw, pitch);
    }

    public void spawnNpc() {
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, colorize("&b&lGardien du temps"));

        npc.spawn(getSpawnNpcLocation(), SpawnReason.CREATE);
        npc.data().setPersistent("territory", getRegion());
        npc.setProtected(true);

        terrDB.getDB().set(getRegion() + ".NPC.ID", npc.getId());
        terrDB.saveDB();
    }

    ////////
    //KOTH//
    ////////

    private BukkitTask task;
    private HashMap<Guild, Integer> leaderboard = new HashMap<>();
    private int count;

    public boolean hasCurrentKoth() {
        return task != null;
    }

    public void startKoth() {
        if(hasCurrentKoth()) return;
        if(!isWeak()) return;
        clearOwner();

        count = 0;
        long TIME_SECOND = 2;

        setKothBar(TIME_SECOND, TIME_SECOND);

        this.task = new BukkitRunnable() {
            @Override
            public void run() {

                if(count < TIME_SECOND) {
                    setKothBar(TIME_SECOND - count, TIME_SECOND);
                    count++;
                    return;
                }

                count = 0;
                Guild greatest = getGuildWithMorePlayers();
                if(greatest == null) {
                    return;
                }

                if(!leaderboard.containsKey(greatest)) {
                    leaderboard.put(greatest, 1);
                    setActuBar(greatest);
                    actualizeScoreboard();
                } else {
                    int current = leaderboard.get(greatest);
                    int after = current + 1;

                    leaderboard.put(greatest, after);
                    main.sendDebug("&7" + greatest.getName() + ": &e" + after);

                    setActuBar(greatest);
                    actualizeScoreboard();
                    if(after >= 30) {
                        task.cancel();
                        task = null;
                        clearScoreboard();
                        leaderboard = new HashMap<>();

                        setPossibleHour(Hour.TWENTY_ONE);
                        setPossibleMinut(Minut.ZERO);

                        setOwner(greatest.getUuid());
                        setWeak(false);
                        setConquerBar(main.getUtils().getUtilsGuild().getGuild(greatest.getUuid()));
                        spawnBoss();

                        main.getUtils().getUtilsGuild().sendMessageToAllGuild(greatest.getUuid(),
                                "&8&l=======================", false);
                        main.getUtils().getUtilsGuild().sendMessageToAllGuild(greatest.getUuid(),
                                "", false);
                        main.getUtils().getUtilsGuild().sendMessageToAllGuild(greatest.getUuid(),
                                "&8>> &e&lNOUVELLE ZONE CONQUIT !", false);
                        main.getUtils().getUtilsGuild().sendMessageToAllGuild(greatest.getUuid(),
                                "", false);
                        main.getUtils().getUtilsGuild().sendMessageToAllGuild(greatest.getUuid(),
                                "&8&l=======================", false);
                    }
                }
            }
        }.runTaskTimer(main, 20, 20);
    }

    public Guild getGuildWithMorePlayers() {
        HashMap<Guild, Integer> map = new HashMap<>();
        for(org.bukkit.entity.Player player : getPlayers()) {
            Guild guild = main.getUtils().getUtilsGuild().getGuild(player);
            if(guild == null) continue;
            if(!map.containsKey(guild)) {
                map.put(guild, 1);
            } else {
                int current = map.get(guild);
                map.put(guild, current + 1);
            }
        }

        LinkedHashMap<Guild, Integer> sorted = sortByValue(map);
        Guild greatest;

        try {
            Map.Entry<Guild,Integer> entry = sorted.entrySet().iterator().next();
            greatest = entry.getKey();
        } catch (NoSuchElementException e) {
            return null;
        }

        return greatest;
    }

    public LinkedHashMap<Guild, Integer> sortByValue(HashMap<Guild, Integer> hm) {
        List<Map.Entry<Guild, Integer> > list =
                new LinkedList<>(hm.entrySet());

        list.sort(new highToLow());

        LinkedHashMap<Guild, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<Guild, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    ////////
    //TIME//
    ////////

    private BukkitTask task_time;

    private final long MINUT = 60000L;
    private final long HOUR = 60 * MINUT;
    private final long DAY = 24 * HOUR;

    public void setStartTime(Hour currentHour, Minut currentMinut) {
        setPossibleHour(currentHour);
        setPossibleMinut(currentMinut);
    }

    public void initTimer() {
        LocalDateTime end = Instant.ofEpochMilli(getPossibleEnd()).atZone(ZoneId.of("Europe/Paris")).toLocalDateTime();
        deleteHologram();
        createHologram(getSpawnNpcLocation().clone().add(0, 4, 0), end);

        if(task_time != null)
            task_time.cancel();


        task_time = main.getServer().getScheduler().runTaskTimer(main, () -> {
            if(Instant.now().toEpochMilli() >= getPossibleEnd())  {
                endWeakening();
                startKoth();

                deleteHologram();
                task_time.cancel();
            }
        }, 0, 20);
    }

    public void setPossibleEnd(Long result) {
        terrDB.getDB().set(getRegion() + ".time.possible-end", result);
        terrDB.saveDB();
    }

    public Long getPossibleEnd() {
        return terrDB.getDB().getLong(getRegion() + ".time.possible-end");
    }

    public void setPossibleHour(Hour result) {
        terrDB.getDB().set(getRegion() + ".time.possible-hour", result.getNumber());
        terrDB.saveDB();
    }

    public Integer getPossibleHour() {
        Object obj = terrDB.getDB().get(getRegion() + ".time.possible-hour");
        if(obj == null) return 21;
        return terrDB.getDB().getInt(getRegion() + ".time.possible-hour");
    }

    public void setPossibleMinut(Minut result) {
        terrDB.getDB().set(getRegion() + ".time.possible-minut", result.getNumber());
        terrDB.saveDB();
    }

    public Integer getPossibleMinut() {
        Object obj = terrDB.getDB().get(getRegion() + ".time.possible-minut");
        if(obj == null) return 0;
        return terrDB.getDB().getInt(getRegion() + ".time.possible-minut");
    }

    public String getFrenchDOFTHW(String input) {
        switch (input.toLowerCase()) {
            case "monday":
                return "Lundi";
            case "tuesday":
                return "Mardi";
            case "wednesday":
                return "Mercredi";
            case "thursday":
                return "Jeudi";
            case "friday":
                return "Vendredi";
            case "saturday":
                return "Samedi";
            case "sunday":
                return "Dimanche";
        }
        return "null";
    }

    public String getFrenchMonth(int input) {
        switch(input) {
            case 1:
                return "Janvier";
            case 2:
                return "Février";
            case 3:
                return "Mars";
            case 4:
                return "Avril";
            case 5:
                return "Mai";
            case 6:
                return "Juin";
            case 7:
                return "Juillet";
            case 8:
                return "Aoüt";
            case 9:
                return "Septembre";
            case 10:
                return "Octobre";
            case 11:
                return "Novembre";
            case 12:
                return "Décembre";
        }
        return null;
    }

    ///////////
    //BOSSBAR//
    ///////////

    private BossBar bar;
    private barType BAR_TYPE;

    public enum barType {
        WEAK, CONQUER, KOTH
    }

    public barType getBarType() {
        return BAR_TYPE;
    }

    public void setType(barType type) {
        this.BAR_TYPE = type;
    }

    public void setConquerBar(Guild guild) {
        if(guild == null) return;
        setType(barType.CONQUER);

        getBar().setVisible(true);
        getBar().setProgress(1);
        getBar().setTitle(colorize("&aTerritoire occupé par la guilde " + guild.getName()));
        getBar().setColor(BarColor.GREEN);
    }

    public void setWeakBar() {
        setType(barType.WEAK);

        getBar().setVisible(true);
        getBar().setProgress(1);
        getBar().setTitle(colorize("&eTerritoire affaiblit."));
        getBar().setColor(BarColor.YELLOW);
    }

    public void setKothBar(double time, long total) {
        setType(barType.KOTH);

        getBar().setVisible(true);
        getBar().setProgress(time / total);
        getBar().setTitle(colorize("&cTemps avant prochaine actualisation: " + (int) time + " secondes"));
        getBar().setColor(BarColor.RED);
    }

    public void setActuBar(Guild guild) {
        setType(barType.KOTH);

        getBar().setVisible(true);
        getBar().setProgress(0);
        getBar().setTitle(colorize("&cPoint gagné par la guilde &f" + guild.getName() + " &c!"));
        getBar().setColor(BarColor.RED);
    }

    public BossBar getBar() {
        return bar;
    }

    //////////////
    //SCOREBOARD//
    //////////////

    public void actualizeScoreboard() {
        HashMap<Guild, Integer> sorted = sortByValue(leaderboard);
        List<String> list = new LinkedList<>();

        for(Map.Entry<Guild, Integer> hm : sorted.entrySet()) {
            String input = colorize("&e" + hm.getKey().getName() + "&8: &7" + hm.getValue());
            list.add(input);
        }

        for(Map.Entry<UUID, FastBoard> board : boards.entrySet()) {
            board.getValue().updateTitle(colorize("&e&lKOTH &8- &e30 points"));
            board.getValue().updateLines(list);
        }
    }

    public void actualizeScoreboardForPlayer(org.bukkit.entity.Player player) {
        HashMap<Guild, Integer> sorted = sortByValue(leaderboard);
        List<String> list = new LinkedList<>();

        for(Map.Entry<Guild, Integer> hm : sorted.entrySet()) {
            String input = colorize("&e" + hm.getKey().getName() + "&8: &7" + hm.getValue());
            list.add(input);
        }

        FastBoard board = boards.get(player.getUniqueId());
        board.updateTitle(colorize("&e&lKOTH &8- &e30 points"));
        board.updateLines(list);

    }

    public void clearScoreboard() {
        List<String> list = new ArrayList<>();
        for(Map.Entry<UUID, FastBoard> board : boards.entrySet()) {
            try {
                board.getValue().updateTitle("");
                board.getValue().updateLines(list);
            } catch (RuntimeException ignored) {
            }
        }
    }

    ////////////
    //HOLOGRAM//
    ////////////

    private Hologram hologram;

    public void createHologram(Location location, LocalDateTime end) {
        deleteHologram();
        hologram = HologramsAPI.createHologram(main, location);
        hologram.appendTextLine(colorize("&e&lKOTH"));
        hologram.appendTextLine(colorize("&e" + getFrenchDOFTHW(end.getDayOfWeek().toString()) + " " +  end.getDayOfMonth() + " " + getFrenchMonth(end.getMonthValue())));
        hologram.appendTextLine(colorize("&eà " + end.getHour() + "H" + end.getMinute()));
    }

    public void deleteHologram() {
        if(hologram != null)
            hologram.delete();
    }

    //////////
    //PLAYER//
    //////////

    public void addPlayer(org.bukkit.entity.Player player) {
        players.add(player);
    }

    public void removePlayer(org.bukkit.entity.Player player) {
        players.remove(player);
    }
}
