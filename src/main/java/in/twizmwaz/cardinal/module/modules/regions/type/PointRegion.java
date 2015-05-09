package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.parsers.PointParser;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class PointRegion extends BlockRegion {

    private final float yaw, pitch;

    public PointRegion(String name, double x, double y, double z, float yaw, float pitch) {
        super(name, x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PointRegion(String name, double x, double y, double z) {
        this (name, x, y, z, 0F, 0F);
    }

    public PointRegion(String name, Vector vector, float yaw, float pitch) {
        this(name, vector.getX(), vector.getY(), vector.getZ(), yaw, pitch);
    }

    public PointRegion(String name, Vector vector) {
        this(name, vector.getX(), vector.getY(), vector.getZ());
    }

    public PointRegion(PointParser parser) {
        this(parser.getName(), parser.getVector(), parser.getYaw(), parser.getPitch());
    }

    @Override
    public Vector getVector() {
        return vector.clone();
    }

    @Override
    public PointRegion getRandomPoint() {
        return this;
    }

    @Override
    public Location getLocation() {
        return new Location(GameHandler.getGameHandler().getMatchWorld(), vector.getX(), vector.getY(), vector.getZ(), getYaw(), getPitch());
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public Block getBlock() {
        return vector.toLocation(GameHandler.getGameHandler().getMatchWorld()).getBlock();
    }

}
