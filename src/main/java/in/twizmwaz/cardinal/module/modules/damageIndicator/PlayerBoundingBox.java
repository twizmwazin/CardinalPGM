package in.twizmwaz.cardinal.module.modules.damageIndicator;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.PacketUtils;
import in.twizmwaz.cardinal.util.Teams;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.DataWatcherRegistry;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntity;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutEntityTeleport;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUseUnknownEntityEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class PlayerBoundingBox implements Listener {

   private static double MAX_DISTANCE = 10;

    private UUID player;
    private Optional<TeamModule> team;
    private List<UUID> viewers = new ArrayList<>();
    private List<Integer> zombieID = new ArrayList<>();
    private int mainTask;

    protected PlayerBoundingBox(UUID player, Optional<TeamModule> team) {
        this.player = player;
        this.team = team;
        for (int i = 0; i < 4; i++) {
            zombieID.add(Bukkit.allocateEntityId());
        }
        Bukkit.getPluginManager().registerEvents(this, Cardinal.getInstance());
        mainTask = Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                updateAndTeleport(null);
            }
        }, 20L);
    }

    public void destroy(){
        Bukkit.getScheduler().cancelTask(mainTask);
        broadcastRemovePacket();
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onZombieAttack(PlayerUseUnknownEntityEvent event) {
        if (shouldShow(event.getPlayer()) && event.isAttack() && this.zombieID.contains(event.getEntityId())) {
            ((CraftPlayer)event.getPlayer()).getHandle().attack(((CraftPlayer)Bukkit.getPlayer(player)).getHandle());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerTeleportEvent event) {
        if (event.getPlayer().getUniqueId().equals(this.player)) {
            updateAndTeleport(event.getTo());
        } else {
            sendOrRemoveZombies(event.getPlayer(), event.getTo(), Bukkit.getPlayer(player).getLocation());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getUniqueId().equals(this.player)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendOrRemoveZombies(player, player.getLocation(), event.getTo());
            }
            Vector diff = event.getTo().clone().subtract(event.getFrom());
            relativeMoveBoundingBox(Math.round(4096 * (diff.getX())),
                    Math.round(4096 * (diff.getY())), Math.round(4096 * (diff.getZ())), event.getPlayer().isOnGround());
        } else {
            sendOrRemoveZombies(event.getPlayer(), event.getTo(), Bukkit.getPlayer(player).getLocation());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(CardinalDeathEvent event) {
        if (event.getPlayer().getUniqueId().equals(this.player)) {
            broadcastRemovePacket();
        } else {
            sendRemovePacket(event.getPlayer());
        }
    }

    private boolean shouldShow(Player player) {
        Optional<TeamModule> team = Teams.getTeamByPlayer(player);
        return !ObserverModule.testObserverOrDead(player) && (team.isPresent() && !team.get().equals(this.team.get()));
    }

    public void sendOrRemoveZombies(Player player, Vector playerPos, Vector bbPos) {
        if (shouldShow(player) && bbPos.distance(playerPos) < MAX_DISTANCE) {
            sendSpawnPackets(player);
        } else {
            sendRemovePacket(player);
        }
    }

    public void updateAndTeleport(Vector loc) {
        if (loc == null) loc = Bukkit.getPlayer(this.player).getLocation();
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendOrRemoveZombies(player, player.getLocation(), loc);
        }
        teleportBoundingBox(loc, Bukkit.getPlayer(this.player).isOnGround());
    }

    public void teleportBoundingBox(Vector to, boolean onGround) {
        int i = 0;
        for (int x = -1; x < 2; x += 2) {
            for (int z = -1; z < 2; z += 2) {
                Packet teleportPacket = new PacketPlayOutEntityTeleport(zombieID.get(i),
                        to.getX() + (x * DamageIndicator.OFFSET), to.getY() , to.getZ() + (z * DamageIndicator.OFFSET),
                        (byte) 2, (byte) 0, onGround);
                PacketUtils.broadcastPacketByUUID(teleportPacket, viewers);
                i++;
            }
        }
    }

    public void relativeMoveBoundingBox(long x, long y, long z, boolean onGround) {
        for (Integer zombie : zombieID) {
            PacketUtils.broadcastPacketByUUID(new PacketPlayOutEntity.PacketPlayOutRelEntityMove(zombie, x, y, z, onGround), viewers);
        }
    }

    public void sendSpawnPackets(Player viewer) {
        if (viewers.contains(viewer.getUniqueId())) return;
        Player player = Bukkit.getPlayer(this.player);
        Location loc = player.getLocation();

        List<DataWatcher.Item<?>> dataItems = new ArrayList<>();
        dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.a.a(0), (byte) 32)); // Sets invisible
        dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.c.a(7), 20.0F));    // Sets health

        int i = 0;
        for (int x = -1; x < 2; x += 2) {
            for (int z = -1; z < 2; z += 2) {
                Packet spawnPacket = new PacketPlayOutSpawnEntityLiving(
                        zombieID.get(i), UUID.randomUUID(), 54,                 // Entity id, UUID, and type (Zombie)
                        loc.getX() + (x * DamageIndicator.OFFSET), loc.getY() , // X, Y
                        loc.getZ() + (z * DamageIndicator.OFFSET),              // and Z coords
                        0, 0, 0,                                                // X, Y and Z Motion
                        (byte)2, (byte)0, (byte)2,                              // Yaw, Pitch and Head Pitch
                        dataItems);                                             // Metadata
                PacketUtils.sendPacket(viewer, spawnPacket);
                i++;
            }
        }
        viewers.add(viewer.getUniqueId());
    }

    private Packet getRemovePacket() {
        int[] ids = ArrayUtils.toPrimitive(zombieID.toArray(new Integer[4]));
        return new PacketPlayOutEntityDestroy(ids);
    }

    public void sendRemovePacket(Player player) {
        if (!viewers.contains(player.getUniqueId())) return;
        PacketUtils.sendPacket(player, getRemovePacket());
        viewers.remove(player.getUniqueId());
    }

    private void broadcastRemovePacket() {
        PacketUtils.broadcastPacketByUUID(getRemovePacket(), viewers);
    }

}
