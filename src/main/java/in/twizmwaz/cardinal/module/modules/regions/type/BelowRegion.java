package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.BelowParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BelowRegion extends RegionModule {

    private final Vector vector;

    public BelowRegion(String name, Vector vector) {
        super(name);
        this.vector = vector;
    }

    public BelowRegion(BelowParser parser) {
        this(parser.getName(), parser.getVector());
    }

    @Override
    public boolean contains(Vector vector) {
        return this.vector.isGreater(vector);
    }

    @Override
    public PointRegion getRandomPoint() {
        Vector min = getMin();
        Vector max = getMax();
        double x = Numbers.getRandom(min.getX(), max.getX());
        double y = Numbers.getRandom(min.getY(), max.getY());
        double z = Numbers.getRandom(min.getZ(), max.getZ());
        return new PointRegion(null, x, y, z);
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, getMin().midpoint(getMax()));
    }

    @Override
    public List<Block> getBlocks() {
        return new ArrayList<>();
    }

    @Override
    public Vector getMin() {
        return new Vector(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    }

    @Override
    public Vector getMax() {
        return vector;
    }

}
