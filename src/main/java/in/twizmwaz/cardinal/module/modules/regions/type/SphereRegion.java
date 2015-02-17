package in.twizmwaz.cardinal.module.modules.regions.type;

import in.parapengu.commons.utils.OtherUtil;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.SphereParser;
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

    public double getOriginx() {
        return origin.getX();
    }

    public double getOriginy() {
        return origin.getY();
    }

    public double getOriginz() {
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
        double a = OtherUtil.getRandom(0, radius);
        double b = OtherUtil.getRandom(0, 360);
        double c = OtherUtil.getRandom(0, radius);
        double d = OtherUtil.getRandom(0, 360);
        double e = OtherUtil.getRandom(0, radius);
        double f = OtherUtil.getRandom(0, 360);
        return new PointRegion(null, getOriginx() + a * Math.sin(b), getOriginy() + c * Math.sin(d), getOriginz() + e * Math.sin(f));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, this.origin);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getOriginx() - radius, getOriginy() - radius, getOriginz() - radius, getOriginx() + radius, getOriginy() + radius, getOriginz() + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
