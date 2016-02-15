package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.SphereParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SphereRegion extends RegionModule {

    private final Vector origin;
    private final double radius;

    public SphereRegion(String name, Vector origin, double radius) {
        super(name);
        this.origin = origin;
        this.radius = radius;
    }

    public SphereRegion(SphereParser parser) {
        this(parser.getName(), parser.getOrigin(), parser.getRadius());
    }

    public double getOriginX() {
        return origin.getX();
    }

    public double getOriginY() {
        return origin.getY();
    }

    public double getOriginZ() {
        return origin.getZ();
    }

    public Vector getOrigin() {
        return origin;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean contains(Vector vector) {
        return vector.isInSphere(getOrigin(), getRadius());
    }

    @Override
    public PointRegion getRandomPoint() {
        double a = Numbers.getRandom(0, radius);
        double b = Numbers.getRandom(0, 360);
        double c = Numbers.getRandom(0, radius);
        double d = Numbers.getRandom(0, 360);
        double e = Numbers.getRandom(0, radius);
        double f = Numbers.getRandom(0, 360);
        return new PointRegion(null, getOriginX() + a * Math.sin(b), getOriginY() + c * Math.sin(d), getOriginZ() + e * Math.sin(f));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, this.origin);
    }

    @Override
    public Vector getCenter() {
        return origin.clone();
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getOriginX() - radius, getOriginY() - radius, getOriginZ() - radius, getOriginX() + radius, getOriginY() + radius, getOriginZ() + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
