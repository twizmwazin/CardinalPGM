package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.BlockParser;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockRegion extends RegionModule {

    private final double x, y, z;

    public BlockRegion(String name, double x, double y, double z) {
        super(name);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockRegion(BlockParser parser) {
        this(parser.getName(), parser.getX(), parser.getY(), parser.getZ());
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

    @Override
    public boolean contains(BlockRegion region) {
        return region.getX() == getX() && region.getY() == getY() && region.getZ() == getZ();
    }

    @Override
    public boolean contains(PointRegion region) {
        return region.getX() == getX() && region.getY() == getY() && region.getZ() == getZ();
    }

    @Override
    public PointRegion getRandomPoint() {
        return new PointRegion(null, x, y, z);
    }

    @Override
    public BlockRegion getCenterBlock() {
        return this;
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        results.add(getBlock());
        return results;
    }

    public Location getLocation() {
        return new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z);
    }

    public Block getBlock() {
        return this.getLocation().getBlock();
    }

}
