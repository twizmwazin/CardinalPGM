package in.twizmwaz.cardinal.module.modules.regions.type;

import in.parapengu.commons.utils.OtherUtil;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.SphereParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class SphereRegion extends RegionModule {

    private final double originX, originY, originZ, radius;

    public SphereRegion(String name, double originX, double originY, double originZ, double radius) {
        super(name);
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.radius = radius;
    }

    public SphereRegion(SphereParser parser) {
        this(parser.getName(), parser.getOriginX(), parser.getOriginY(), parser.getOriginZ(), parser.getRadius());
    }

    public double getOriginx() {
        return originX;
    }

    public double getOriginy() {
        return originY;
    }

    public double getOriginz() {
        return originZ;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return NumUtils.hypotSphere(region.getX(), region.getY(), region.getZ()) <= getRadius();
    }

    @Override
    public boolean contains(PointRegion region) {
        return NumUtils.hypotSphere(region.getX(), region.getY(), region.getZ()) <= getRadius();
    }

    @Override
    public PointRegion getRandomPoint() {
        double a = OtherUtil.getRandom(0, radius);
        double b = OtherUtil.getRandom(0, 360);
        double c = OtherUtil.getRandom(0, radius);
        double d = OtherUtil.getRandom(0, 360);
        double e = OtherUtil.getRandom(0, radius);
        double f = OtherUtil.getRandom(0, 360);
        return new PointRegion(null, originX + a * Math.sin(b), originY + c * Math.sin(d), originZ + e * Math.sin(f));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, this.getOriginx(), this.getOriginy(), this.originZ);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, originX - radius, originY - radius, originZ - radius, originX + radius, originY + radius, originZ + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
