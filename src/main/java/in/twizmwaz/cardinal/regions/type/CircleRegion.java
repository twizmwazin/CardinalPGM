package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.regions.parsers.CircleParser;

/**
 * Created by kevin on 10/26/14.
 */
public class CircleRegion extends CylinderRegion {

    public CircleRegion(String name, double centerx, double centerz, double radius) {
        super(name, centerx, Double.NEGATIVE_INFINITY, centerz, radius, Double.POSITIVE_INFINITY);
    }

    public CircleRegion(CircleParser parser) {
        super(parser.getName(), parser.getCenterX(), Double.NEGATIVE_INFINITY, parser.getCenterZ(), parser.getRadius(), Double.POSITIVE_INFINITY);
    }

    public String getName() {
        return super.getName();
    }

    public double getCenterX() {
        return super.getBaseX();
    }

    public double getCenterZ() {
        return super.getBaseZ();
    }

    public double getRadius() {
        return super.getRadius();
    }

    @Override
    public boolean contains(BlockRegion region) {
        return super.contains(region);
    }
}
