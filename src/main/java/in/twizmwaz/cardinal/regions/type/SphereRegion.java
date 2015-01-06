package in.twizmwaz.cardinal.regions.type;


import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.SphereParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public class SphereRegion extends Region {

    private double originX;
    private double originY;
    private double originZ;
    private double radius;

    public SphereRegion(String name, double originX, double originY, double originZ, double radius) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.radius = radius;
    }

    public SphereRegion(SphereParser parser) {
        this.originX = parser.getOriginx();
        this.originY = parser.getOriginy();
        this.originZ = parser.getOriginz();
        this.radius = parser.getRadius();
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
        double a = NumUtils.randomInterval(0, radius);
        double b = NumUtils.randomInterval(0, 360);

        double c = NumUtils.randomInterval(0, radius);
        double d = NumUtils.randomInterval(0, 360);

        double e = NumUtils.randomInterval(0, radius);
        double f = NumUtils.randomInterval(0, 360);

        return new PointRegion(originX + a * Math.sin(b), originY + c * Math.sin(d), originZ + e * Math.sin(f));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(this.getOriginx(), this.getOriginy(), this.originZ);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(originX - radius, originY - radius, originZ - radius, originX + radius, originY + radius, originZ + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }

}
