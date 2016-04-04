package in.twizmwaz.cardinal.module.modules.damageIndicator;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Teams;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.DataWatcherRegistry;
import net.minecraft.server.PacketPlayOutEntity;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Cuboid;
import org.bukkit.util.Ray;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DamageIndicator implements Module {

    private static double WIDTH = 0.75;
    private static double ZOMBIE_HIT_BOX = 0.6;

    public static double OFFSET = (WIDTH - ZOMBIE_HIT_BOX) / 2;

    private HashMap<UUID, PlayerBoundingBox> boundingBoxes = new HashMap<>();

    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeBoundingBox(player);
        }
        HandlerList.unregisterAll(this);
    }

    private PlayerBoundingBox getBoundingBox(Player player) {
        if (!boundingBoxes.containsKey(player.getUniqueId())) boundingBoxes.put(player.getUniqueId(),
                new PlayerBoundingBox(player.getUniqueId(), Teams.getTeamByPlayer(player)));
        return boundingBoxes.get(player.getUniqueId());
    }

    private void removeBoundingBox(Player player) {
        if (!boundingBoxes.containsKey(player.getUniqueId())) return;
        boundingBoxes.get(player.getUniqueId()).destroy();
        boundingBoxes.remove(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        removeBoundingBox(event.getPlayer());
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Optional<TeamModule> team = Teams.getTeamByPlayer(player);
            if (!team.isPresent() || !team.get().isObserver()) {
                getBoundingBox(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (!GameHandler.getGameHandler().getMatch().isRunning()) return;
        removeBoundingBox(event.getPlayer());
        if (!event.getNewTeam().isPresent() || !event.getNewTeam().get().isObserver()) {
            getBoundingBox(event.getPlayer());
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (boundingBoxes.containsKey(player.getUniqueId())) {
                boundingBoxes.get(player.getUniqueId()).destroy();
                boundingBoxes.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player)event.getDamager();
            if (Settings.getSettingByName("DamageNumbers").getValueByPlayer(player).getValue().equalsIgnoreCase("off")) return;
            Vector loc = getHitPosition(player.getEyeRay(), (Player)event.getEntity());
            if (loc == null) return;
            List<DataWatcher.Item<?>> dataItems = new ArrayList<>();
            dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.a.a(0), (byte) 32));            // Sets invisible
            dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.d.a(2),
                    "" + ChatColor.RED + ChatColor.BOLD + Math.round(event.getFinalDamage() / 0.2)));// Custom Name
            dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.h.a(3), true));                 // Custom Name visible
            dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.a.a(10),(byte)0x10));           // Marker Armor Stand
            dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.c.a(6), 20.0F));                // Sets health

            int id = Bukkit.allocateEntityId();
            timeAndDestroyEntity(id, player);

            PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(
                    id, UUID.randomUUID(),                    // Entity id and Entity UUID
                    30,                                       // Entity type id (ArmorStand)
                    loc.getX(), loc.getY() - 0.4D, loc.getZ(),// X, Y and Z Position
                    0, 0, 0,                                  // X, Y and Z Motion
                    (byte) 2, (byte) 0, (byte) 2,             // Yaw, Pitch and Head Pitch
                    dataItems);                               // Metadata

            PacketUtils.sendPacket(player, spawnPacket);
        }
    }

    private void timeAndDestroyEntity(final int id, final Player player) {
        final int teleport = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.sendPacket(player, new PacketPlayOutEntity.PacketPlayOutRelEntityMove(id, 0L, 32L, 0L, false));
            }
        }, 1L , 1L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                PacketUtils.sendPacket(player, new PacketPlayOutEntityDestroy(id));
                Bukkit.getServer().getScheduler().cancelTask(teleport);
            }
        }, 20L);
    }

    private static Vector getHitPosition(Ray ray, Player player) {
        Cuboid oldBB = player.getBoundingBox();
        Vector min = oldBB.minimum().minus(OFFSET, 0, OFFSET);
        Vector size = new Vector(WIDTH, oldBB.size().getY(), WIDTH);
        return Cuboid.fromMinAndSize(min, size).intersect(ray);
    }

}
