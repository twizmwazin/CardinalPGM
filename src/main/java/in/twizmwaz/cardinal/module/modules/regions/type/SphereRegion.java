package in.twizmwaz.cardinal.module.modules.regions.type;


import in.twizmwaz.cardinal.module.modules.regions.Region;
import in.twizmwaz.cardinal.module.modules.regions.parsers.SphereParser;
import in.twizmwaz.cardinal.util.NumUtils;

/**
 * Created by kevin on 10/26/14.
 */
public class SphereRegion extends Region {

    private String name;
    private double originX;
    private double originY;
    private double originZ;
    private double radius;

    public SphereRegion(String name, double originX, double originY, double originZ, double radius) {
        super(name);
        this.name = name;
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
        this.radius = radius;
    }

    public SphereRegion(SphereParser parser) {
        super(parser.getName());
        this.name = parser.getName();
        this.originX = parser.getOriginx();
        this.originY = parser.getOriginy();
        this.originZ = parser.getOriginz();
        this.radius = parser.getRadius();
    }

    public String getName() {
        return name;
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
        return NumUtils.hypotsphere(region.getX(), region.getY(), region.getZ()) <= getRadius();
    }
}
