package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.parsers.RectangleParser;

/**
 * Created by kevin on 10/26/14.
 */
public class RectangleRegion extends CuboidRegion {


    public RectangleRegion(String name, double xMin, double zMin, double xMax, double zMax) {
        super(name, xMin, Double.NEGATIVE_INFINITY, zMin, xMax, Double.POSITIVE_INFINITY, zMax);
    }

    public RectangleRegion(RectangleParser parser) {
        super(parser.getName(), parser.getXMin(), Double.NEGATIVE_INFINITY, parser.getZMin(), parser.getXMax(), Double.POSITIVE_INFINITY, parser.getZMax());
    }

    public String getName() {
        return super.getName();
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


}
