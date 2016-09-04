package in.twizmwaz.cardinal.module.modules.arrows;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.PlayerSettingChangeEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.SettingValue;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Teams;
import net.minecraft.server.EnumParticle;
import net.minecraft.server.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class ArrowModule implements Module {

    private ArrayList<Integer> tasks = Lists.newArrayList();
    private static List<Arrow> criticals = Lists.newArrayList();

    private static List<Player> allArrows = Lists.newArrayList();
    private static List<Player> selfArrows = Lists.newArrayList();

    @Override
    public void unload() {
        for (Integer task : tasks) {
            Bukkit.getScheduler().cancelTask(task);
        }
        criticals.clear();
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArrowLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow && event.getActor() instanceof Player) {
            Optional<TeamModule> team = Teams.getTeamByPlayer((Player) event.getActor());
            if (team.isPresent()) {
                Arrow arrow = (Arrow) event.getEntity();
                if (arrow.isCritical()) {
                    criticals.add(arrow);
                    arrow.setCritical(false);
                }
                ArrowRunnable runnable = new ArrowRunnable(arrow, team.get().getColor());
                int newTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cardinal.getInstance(), runnable, 1L, 1L);
                runnable.setTask(newTask);
                tasks.add(newTask);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && criticals.contains(event.getEntity())) {
            criticals.remove(event.getEntity());
            ((Arrow) event.getEntity()).setCritical(true);
        }
    }


    public static void sendArrowParticle(Arrow arrow, float x, float y, float z) {
        Location loc = arrow.getLocation();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true,
                (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), x, y, z, 1f, 0);
        PacketUtils.broadcastPacket(packet, allArrows);
        if (arrow.getShooter() instanceof Player && selfArrows.contains(arrow.getShooter())) {
            PacketUtils.sendPacket((Player) arrow.getShooter(), packet);
        }
    }

    public static void arrowOnGround(Arrow arrow) {
        if (criticals.contains(arrow)) {
            criticals.remove(arrow);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerSettings(event.getPlayer(),
                Settings.getSettingByName("ArrowParticles").getValueByPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerChangeSettings(PlayerSettingChangeEvent event) {
        if (event.getSetting().equals(Settings.getSettingByName("ArrowParticles"))) {
            updatePlayerSettings(event.getPlayer(), event.getNewValue());
        }
    }

    private static void updatePlayerSettings(Player player, SettingValue value) {
        resetPlayerSettings(player);
        switch (value.getValue()) {
            case "all":
                allArrows.add(player);
                break;
            case "self":
                selfArrows.add(player);
                break;
        }
    }

    private static void resetPlayerSettings(Player player) {
        allArrows.remove(player);
        selfArrows.remove(player);
    }

}
