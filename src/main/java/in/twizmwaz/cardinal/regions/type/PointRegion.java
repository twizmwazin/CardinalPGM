package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.regions.parsers.PointParser;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

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

    public Location toLocation() {
        return new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z, yaw, pitch);
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(this.x, this.y, this.z);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        results.add(toLocation().getBlock());
        return results;
    }

    public Block getBlock() {
        return this.toLocation().getBlock();
    }
}
