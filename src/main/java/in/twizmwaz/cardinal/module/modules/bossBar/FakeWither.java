package in.twizmwaz.cardinal.module.modules.bossBar;

import net.minecraft.server.v1_8_R1.DataWatcher;
import net.minecraft.server.v1_8_R1.EntityWither;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R1.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

import java.lang.reflect.Field;

public class FakeWither {

    public float health = 0;
    public String name;
    private float maxHealth = 200;
    private Location location;
    private byte xvel = 0;
    private byte yvel = 0;
    private byte zvel = 0;
    private boolean visible = false;
    private EntityWither wither;
    private int id;

    public FakeWither(String name, Location location, int percent) {
        this.name = name;
        this.location = location;
        this.health = percent / 100F * maxHealth;
    }

    public FakeWither(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float max) {
        maxHealth = max;
    }

    public void setHealth(int percent) {
        this.health = percent / 100F * maxHealth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public byte getXvel() {
        return xvel;
    }

    public void setXvel(byte xvel) {
        this.xvel = xvel;
    }

    public byte getYvel() {
        return yvel;
    }

    public void setYvel(byte yvel) {
        this.yvel = yvel;
    }

    public byte getZvel() {
        return zvel;
    }

    public void setZvel(byte zvel) {
        this.zvel = zvel;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public void setWorld(World world) {
        this.location.setWorld(world);
    }

    public PacketPlayOutSpawnEntityLiving getSpawnPacket() {
        wither = new EntityWither(((CraftWorld) getWorld()).getHandle());
        wither.setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
        wither.setInvisible(true);
        wither.setCustomName(name);
        wither.setHealth(health);
        wither.motX = getXvel();
        wither.motY = getYvel();
        wither.motZ = getZvel();
        this.id = wither.getId();
        return new PacketPlayOutSpawnEntityLiving(wither);
    }

    public PacketPlayOutEntityDestroy getDestroyPacket() {
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy();
        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet, new int[]{id});
        } catch (SecurityException | NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return packet;
    }

    public PacketPlayOutEntityMetadata getMetaPacket(DataWatcher watcher) {
        return new PacketPlayOutEntityMetadata(id, watcher, true);
    }

    public PacketPlayOutEntityTeleport getTeleportPacket(Location loc) {
        return new PacketPlayOutEntityTeleport(this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360), false);
    }

    public DataWatcher getWatcher() {
        DataWatcher watcher = new DataWatcher(wither);
        watcher.a(5, isVisible() ? (byte) 0 : (byte) 0x20);
        watcher.a(6, health);
        watcher.a(7, 0);
        watcher.a(8, (byte) 0);
        watcher.a(10, name);
        watcher.a(11, (byte) 1);
        return watcher;
    }
}
