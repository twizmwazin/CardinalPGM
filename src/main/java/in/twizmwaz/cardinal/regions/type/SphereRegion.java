package in.twizmwaz.cardinal.regions.type;


import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.SphereParser;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.WorldPoint;

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
    public boolean contains(WorldPoint region) {
        return NumUtils.hypotSphere(region.getX(), region.getY(), region.getZ()) <= getRadius();
    }

    @Override
    public WorldPoint getRandomPoint() {
        double a = NumUtils.randomInterval(0, radius);
        double b = NumUtils.randomInterval(0, 360);

        double c = NumUtils.randomInterval(0, radius);
        double d = NumUtils.randomInterval(0, 360);

        double e = NumUtils.randomInterval(0, radius);
        double f = NumUtils.randomInterval(0, 360);

        return new WorldPoint(a * Math.sin(b), c * Math.sin(d), e * Math.sin(d));
    }
}
