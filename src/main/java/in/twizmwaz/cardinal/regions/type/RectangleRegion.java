package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.regions.parsers.RectangleParser;

/**
 * Created by kevin on 10/26/14.
 */
public class RectangleRegion extends CuboidRegion {


    public RectangleRegion(String name, double xMin, double zMin, double xMax, double zMax) {
        super(xMin, 0, zMin, xMax, 256, zMax);
    }

    public RectangleRegion(RectangleParser parser) {
        super(parser.getXMin(), Double.NEGATIVE_INFINITY, parser.getZMin(), parser.getXMax(), Double.POSITIVE_INFINITY, parser.getZMax());
    }

    public double getXMin() {
        return super.getXMin();
    }

    public double getZMin() {
        return super.getZMin();
    }

    public double getXMax() {
        return super.getXMax();
    }

    public double getZMax() {
        return super.getZMax();
    }

    @Override
    public boolean contains(BlockRegion region) {
        return super.contains(region);
    }

    @Override
    public PointRegion getRandomPoint() {
        return super.getRandomPoint();
    }


}
