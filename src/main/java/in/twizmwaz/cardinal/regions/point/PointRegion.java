package in.twizmwaz.cardinal.regions.point;

import in.twizmwaz.cardinal.regions.type.BlockRegion;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by kevin on 11/20/14.
 */
public class PointRegion extends BlockRegion {

    private final double x, y, z;
    private final float yaw, pitch;

    public PointRegion(double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PointRegion(PointParser parser) {
        this(parser.getX(), parser.getY(), parser.getZ(), parser.getYaw(), parser.getPitch());
    }

    public PointRegion(double x, double y, double z) {
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
