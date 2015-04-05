package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.BlockParser;
import in.twizmwaz.cardinal.util.FlooredVector;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockRegion extends RegionModule {

    protected final Vector vector;

    public BlockRegion(String name, Vector vector) {
        super(name);
        this.vector = new FlooredVector(vector);
    }
    
    public BlockRegion(String name, double x, double y, double z) {
        super(name);
        this.vector = new FlooredVector(x, y, z);
    }

    public BlockRegion(BlockParser parser) {
        this(parser.getName(), parser.getVector());
    }

    public double getX() {
        return vector.getX() + 0.5;
    }

    public double getY() {
        return vector.getY() + 0.5;
    }

    public double getZ() {
        return vector.getZ() + 0.5;
    }

    public Vector getVector() {
        return vector.clone().add(new Vector(0.5, 0.5, 0.5));
    }
    
    public Vector getFlooredVector() {
        return new FlooredVector(vector);
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.getBlockX() == getVector().getBlockX() &&
                vector.getBlockY() == getVector().getBlockY() &&
                vector.getBlockZ() == getVector().getBlockZ();
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
        return vector.toLocation(GameHandler.getGameHandler().getMatchWorld()).getBlock();
    }

}
