package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.regions.parsers.CircleParser;
import in.twizmwaz.cardinal.util.WorldPoint;

/**
 * Created by kevin on 10/26/14.
 */
public class CircleRegion extends CylinderRegion {

    public CircleRegion(String name, double centerx, double centerz, double radius) {
        super(centerx, 0, centerz, radius, 256);
    }

    public CircleRegion(CircleParser parser) {
        super(parser.getCenterX(), 0, parser.getCenterZ(), parser.getRadius(), 256);
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

    @Override
    public boolean contains(WorldPoint region) {
        return super.contains(region);
    }

    @Override
    public WorldPoint getRandomPoint() {
        return super.getRandomPoint();
    }
}
