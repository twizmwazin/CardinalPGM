package in.twizmwaz.cardinal.module.modules.bossBar;

import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class FakeWither {

    public float health = 0;
    private float maxHealth = 100 / 3;
    private float minHealth = 1;

    private Location location;
    public String name;
    private EntityWither wither;

    private byte xvel = 0;
    private byte yvel = 0;
    private byte zvel = 0;

    private boolean visible = false;

    private int id;

    public FakeWither(String name, Location location, int percent) {
        this.name = name;
        this.location = location;
        this.health = percent / 100F * maxHealth;
    }

    public FakeWither(Player player, String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float max) {
        maxHealth = max;
    }

    public float getMinHealth() {
        return minHealth;
    }

    public void setMinHealth(float min) {
        minHealth = min;
    }

    public void setHealth(int percent) {
        this.health = percent / 100F * maxHealth;
        if (this.health <= this.minHealth) {
            BossBar.delete();
        }
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
        if (visible == this.visible) { return; }
        if (visible) {
            this.visible = true;
        } else {
            BossBar.delete();
        }
    }

    public World getWorld() {
        return location.getWorld();
    }

    public void setWorld(World world) {
        this.location.setWorld(world);
    }

    public void run() {
        this.health -= this.minHealth;
        if (this.health <= this.minHealth || this.health > this.maxHealth) {
            BossBar.delete();
        }
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

    public DataWatcher updateDataWatcher() {
        DataWatcher watcher = new DataWatcher(wither);
        watcher.a(0, (byte) (0 | 1 << 5)); // Make it invisible
        watcher.a(2, this.name); // Name of the bar
        watcher.a(5, isVisible() ? (byte) 0 : (byte) 0); // Visibility of the wither
        watcher.a(6, health); // Health of the wither
        watcher.a(7, 0); // Pottion effect color, zero is invisible
        watcher.a(8, (byte) 0); // Potion effect ambient
        watcher.a(10, name); // Name of the wither
        watcher.a(11, (byte) 1); // Makes the bar still working if the player isn't moving
        watcher.a(15, 1); // Prevent wither from moving
        watcher.a(17, 0); // Watched target
        watcher.a(18, 0); // Watched target
        watcher.a(19, 0); // Watched target
        watcher.a(20, 890); // Invul, makes the wither tiny
        return watcher;
    }

}