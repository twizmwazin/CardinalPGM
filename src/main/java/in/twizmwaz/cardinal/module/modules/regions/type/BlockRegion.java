package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.BlockParser;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockRegion extends RegionModule {

    private final Vector vector;

    public BlockRegion(String name, Vector vector) {
        super(name);
        this.vector = vector;
    }
    
    public BlockRegion(String name, double x, double y, double z) {
        super(name);
        this.vector = new BlockVector(x, y, z);
    }

    public BlockRegion(BlockParser parser) {
        this(parser.getName(), parser.getVector());
    }

    public double getX() {
        return vector.getX();
    }

    public double getY() {
        return vector.getY();
    }

    public double getZ() {
        return vector.getZ();
    }

    public Vector getVector() {
        return vector;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return region.getVector().getBlockX() == getVector().getBlockX() &&
                region.getVector().getBlockY() == getVector().getBlockY() &&
                region.getVector().getBlockZ() == getVector().getBlockZ();
    }

    @Override
    public PointRegion getRandomPoint() {
        return new PointRegion(null, getVector());
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
        return getVector().toLocation(GameHandler.getGameHandler().getMatchWorld());
    }

    public Block getBlock() {
        return this.getVector().toLocation(GameHandler.getGameHandler().getMatchWorld()).getBlock();
    }

}
