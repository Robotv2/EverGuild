package fr.robotv2.guildconquest.territory.listeners;

import fr.robotv2.guildconquest.main;
import fr.robotv2.guildconquest.object.Territory;
import fr.robotv2.guildconquest.territory.utilsTerritory;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static fr.robotv2.guildconquest.utils.utilsGen.colorize;

public class bossListeners implements Listener {

    public HashMap<ActiveMob, BukkitTask> tasks = new HashMap<>();
    public List<ActiveMob> invicible = new ArrayList<>();

    private final main main;
    private final BukkitAPIHelper helper;
    private final utilsTerritory util;
    public bossListeners(main main) {
        this.main = main;
        this.helper = MythicMobs.inst().getAPIHelper();
        this.util = main
                .getTerritory()
                .getUtil();
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if(!helper.isMythicMob(e.getEntity())) return;
        if(!(e.getTarget() instanceof Player)) return;

        Player player = (Player) e.getTarget();
        UUID guildUUID = main.getUtils().getCache().getCache(player);
        Entity entity = e.getEntity();
        ActiveMob mob = helper.getMythicMobInstance(entity);

        if(!util.hasGuild(mob)) return;
        if(util.getGuildFromMob(mob).equals(guildUUID))
            e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(!helper.isMythicMob(e.getEntity())) return;
        if(!(e.getDamager() instanceof Player)) return;

        Player player = (Player) e.getDamager();
        Entity entity = e.getEntity();
        LivingEntity livingEnt = (LivingEntity) entity;

        ActiveMob mob = helper.getMythicMobInstance(entity);
        Territory territoire = util.getTerritoryFromMob(mob);
        UUID DAMAGER_GUILD = main.getUtils().getCache().getCache(player);

        if(territoire == null) return;

        if(invicible.contains(mob)) {
            e.setCancelled(true);
            player.sendMessage(colorize(main.prefix + "&cLe boss est invicible pour le moment. Revenez plus tard."));
            return;
        }

        if(e.getFinalDamage() >= livingEnt.getHealth()) {
            territoire.startWeakening();
            territoire.getBossTask().cancel();
            return;
        }

        if(!territoire.hasOwner()) {
            startRegen(mob, territoire);
            if(!mob.getEntity().hasAI())
                mob.getEntity().setAI(true);
            return;
        }

        if(territoire.getOwner().equals(DAMAGER_GUILD)) {
            main.getUtils().getUtilsGuild().sendMessageToAllGuild(DAMAGER_GUILD, main.prefix + "&cTentative de sabotage sur votre zone: " + territoire.getDisplay(), false);
        } else {
            main.getUtils().getUtilsGuild().sendMessageToAllGuild(territoire.getOwner(),main.prefix +  "&cQuelqu'un est entrain d'attaquer votre zone: " + territoire.getDisplay(), false);
            startRegen(mob, territoire);
            if(!mob.getEntity().hasAI())
                mob.getEntity().setAI(true);
        }
    }

    public void startRegen(ActiveMob mob, Territory territoire) {
        if(tasks.containsKey(mob))
            tasks.get(mob).cancel();

        BukkitTask runnable = main.getServer().getScheduler().runTaskLater(main, () -> {
                invicible.add(mob);
                mob.getEntity().setHealth(mob.getEntity().getMaxHealth());
                mob.getEntity().setAI(false);
                mob.getEntity().teleport(BukkitAdapter.adapt(territoire.getSpawnLocation()));
                startNormalState(mob);
        },20 * 60 * 10);
        tasks.put(mob, runnable);
    }

    public void startNormalState(ActiveMob mob) {
        main.getServer().getScheduler().runTaskLater(main, () -> {
            invicible.remove(mob);
        }, 20 * 60 * 60 * 2);
    }
}
