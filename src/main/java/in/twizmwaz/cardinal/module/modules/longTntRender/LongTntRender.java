package in.twizmwaz.cardinal.module.modules.longTntRender;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.event.PlayerSettingChangeEvent;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.settings.Setting;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.PacketUtils;
import net.minecraft.server.DataWatcher;
import net.minecraft.server.DataWatcherRegistry;
import net.minecraft.server.EnumItemSlot;
import net.minecraft.server.Packet;
import net.minecraft.server.PacketPlayOutEntityDestroy;
import net.minecraft.server.PacketPlayOutEntityEquipment;
import net.minecraft.server.PacketPlayOutEntityTeleport;
import net.minecraft.server.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockDispenseEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LongTntRender implements TaskedModule {

    private static Map<Integer, Float> distances = new HashMap<>();

    static {
        /*
        The distance a player can see tnt at is dependent on the client's render distance, it's about half their render
        distance, but that isn't quite accurate, the most accurate number is 7.84 blocks per chunk in render distance.
        Bellow 8 or above 20 will use 8 or 20.
        I'm also saving the squared number, so we don't need to calc a square root when calculating distance.
         */
        for (int i = 8; i < 21; i++) distances.put(i, (float) Math.pow(i * 7.84, 2));
    }

    private List<FakeTnt> fakeTnt = Lists.newArrayList();

    private List<TNTPrimed> toAdd = new ArrayList<>();
    private List<FakeTnt> toRemove = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    public static final Setting setting = Settings.getSettingByName("TntRendering");

    public LongTntRender() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!setting.getValueByPlayer(player).getValue().equals("off")) {
                players.add(player);
            }
        }
    }

    private float getTntDistance(Player player) {
        Integer dist = player.getSettings().getRenderDistance();
        if (dist == null || dist < 8) return distances.get(8);
        if (dist > 20) return distances.get(20);
        return distances.get(dist);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void run() {
        for (TNTPrimed tnt : toAdd) fakeTnt.add(new FakeTnt(tnt));
        toAdd.clear();
        for (FakeTnt tnt : fakeTnt) {
            if (tnt.tnt.isDead()) {
                toRemove.add(tnt);
            } else {
                Location loc = tnt.tnt.getLocation();
                for (Player player : players) {
                    if (loc.distanceSquared(player.getLocation()) > getTntDistance(player)) {
                        if (tnt.addPlayer(player)) createFakePlayerPacket(player, tnt);
                    } else if (tnt.removePlayer(player)) PacketUtils.sendPacket(player, removeFakePlayerPacket(tnt));
                }
                PacketUtils.broadcastPacketByUUID(movePacket(tnt), tnt.viewers);
            }
        }
        for (FakeTnt tnt : toRemove) {
            PacketUtils.broadcastPacket(removeFakePlayerPacket(tnt), players);
            fakeTnt.remove(tnt);
        }
        toRemove.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!setting.getValueByPlayer(event.getPlayer()).getValue().equals("none")) {
            players.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onSettingChange(PlayerSettingChangeEvent event) {
        if (!event.getSetting().equals(setting)) return;
        Player player = event.getPlayer();
        String newValue = event.getNewValue().getValue();
        if (newValue.equals("off")) {
            if (players.contains(player)) players.remove(player);
            for (FakeTnt tnt : fakeTnt) {
                if (tnt.removePlayer(player)) PacketUtils.sendPacket(player, removeFakePlayerPacket(tnt));
            }
        } else if (!players.contains(player)) {
            players.add(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        players.remove(event.getPlayer());
        for (FakeTnt tnt : fakeTnt) tnt.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onTntSpawn(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) toAdd.add((TNTPrimed) event.getEntity());
    }

    @EventHandler
    public void onTntDispense(BlockDispenseEntityEvent event) {
        if (event.getEntity() instanceof TNTPrimed) toAdd.add((TNTPrimed) event.getEntity());
    }

    @EventHandler
    public void onTntExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) return;
        Location actual = event.getLocation();
        for (Player player : players) {
            if (actual.distance(player.getLocation()) >= 64.0f)
                player.playEffect(actual, Effect.EXPLOSION_HUGE, 0, 0, 0f, 0f, 0f, 1f, 256, 1);
        }
    }

    public void createFakePlayerPacket(Player player, FakeTnt tnt) {
        Location loc = tnt.tnt.getLocation();

        List<DataWatcher.Item<?>> dataItems = new ArrayList<>();
        dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.a.a(0), (byte) 32)); // Sets invisible
        dataItems.add(new DataWatcher.Item<>(DataWatcherRegistry.c.a(7), 20.0F));     // Sets health

        PacketPlayOutSpawnEntityLiving spawnPacket = new PacketPlayOutSpawnEntityLiving(
                tnt.id, UUID.randomUUID(),                // Entity id and Entity UUID
                30,                                       // Entity type id (ArmorStand)
                loc.getX(), loc.getY() - 1.2D, loc.getZ(),// X, Y and Z Position
                0, 0, 0,                                  // X, Y and Z Motion
                (byte)2, (byte)0, (byte)2,                // Yaw, Pitch and Head Pitch
                dataItems                                 // Metadata
        );
        PacketPlayOutEntityEquipment armorPacket = new PacketPlayOutEntityEquipment(tnt.id, EnumItemSlot.HEAD,
                CraftItemStack.asNMSCopy(new ItemStack(Material.TNT)));

        PacketUtils.sendPacket(player, spawnPacket);
        PacketUtils.sendPacket(player, armorPacket);
    }


    public Packet movePacket(FakeTnt tnt) {
        Location loc = tnt.tnt.getLocation();
        return new PacketPlayOutEntityTeleport(tnt.id, loc.getX(), loc.getY() - 1.2D, loc.getZ(), (byte) 0, (byte) 0, false);
    }

    public Packet removeFakePlayerPacket(FakeTnt tnt) {
        return new PacketPlayOutEntityDestroy(tnt.id);
    }

    private class FakeTnt {

        private TNTPrimed tnt;
        private int id = Bukkit.allocateEntityId();
        private List<UUID> viewers = Lists.newArrayList();

        private FakeTnt(TNTPrimed tnt) {
            this.tnt = tnt;
        }

        private boolean addPlayer(Player player) {
            if (!viewers.contains(player.getUniqueId())) {
                viewers.add(player.getUniqueId());
                return true;
            }
            return false;
        }

        private boolean removePlayer(Player player) {
            if (viewers.contains(player.getUniqueId())) {
                viewers.remove(player.getUniqueId());
                return true;
            }
            return false;
        }

    }

}