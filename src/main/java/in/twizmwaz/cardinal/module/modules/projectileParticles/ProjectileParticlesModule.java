package in.twizmwaz.cardinal.module.modules.projectileParticles;

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
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

public class ProjectileParticlesModule implements Module {

    private List<Integer> tasks = Lists.newArrayList();

    private static List<UUID> allArrows = Lists.newArrayList();
    private static List<UUID> selfArrows = Lists.newArrayList();

    @Override
    public void unload() {
        for (Integer task : tasks) {
            Bukkit.getScheduler().cancelTask(task);
        }
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArrowLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow)
            ((Arrow) event.getEntity()).setCritical(false);
        if (event.getActor() instanceof Player)
            createParticlesFor(event.getEntity(), (Player) event.getActor());
    }

    private void createParticlesFor(Projectile projectile, Player player) {
        TeamModule team = Teams.getTeamOrPlayerByPlayer(player).orNull();
        if (team != null) {
            ProjectileParticleRunnable runnable = new ProjectileParticleRunnable(projectile, team.getColor());
            int newTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cardinal.getInstance(), runnable, 1L, 1L);
            runnable.setTask(newTask);
            tasks.add(newTask);
        }
    }


    public static void sendArrowParticle(Projectile arrow, float x, float y, float z) {
        Location loc = arrow.getLocation();
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true,
                (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), x, y, z, 1f, 0);
        PacketUtils.broadcastPacketByUUID(packet, allArrows);
        if (arrow.getShooter() instanceof Player && selfArrows.contains(((Player) arrow.getShooter()).getUniqueId()))
            PacketUtils.sendPacket((Player) arrow.getShooter(), packet);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerSettings(event.getPlayer(),
                Settings.getSettingByName("ArrowParticles").getValueByPlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerChangeSettings(PlayerSettingChangeEvent event) {
        if (event.getSetting().equals(Settings.getSettingByName("ArrowParticles")))
            updatePlayerSettings(event.getPlayer(), event.getNewValue());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        resetPlayerSettings(event.getPlayer());
    }

    private static void updatePlayerSettings(Player player, SettingValue value) {
        resetPlayerSettings(player);
        switch (value.getValue()) {
            case "all":
                allArrows.add(player.getUniqueId());
                break;
            case "self":
                selfArrows.add(player.getUniqueId());
                break;
        }
    }

    private static void resetPlayerSettings(Player player) {
        allArrows.remove(player.getUniqueId());
        selfArrows.remove(player.getUniqueId());
    }

}
