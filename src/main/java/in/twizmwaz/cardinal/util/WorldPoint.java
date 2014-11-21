package in.twizmwaz.cardinal.util;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by kevin on 11/20/14.
 */
public class WorldPoint {

    private final double x, y, z;
    private final float yaw, pitch;

    public WorldPoint(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public WorldPoint(double x, double y, double z) {
        this(x, y, z, 0F, 0F);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Location toLocation(World world) {
        return new Location(world, x, y,z, yaw, pitch);
    }
}
