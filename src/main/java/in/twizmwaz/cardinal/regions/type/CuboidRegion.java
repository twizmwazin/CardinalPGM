package in.twizmwaz.cardinal.regions.type;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.CuboidParser;
import in.twizmwaz.cardinal.util.NumUtils;

/**
 * Created by kevin on 10/26/14.
 */
public class CuboidRegion extends Region {

    private double xMin;
    private double yMin;
    private double zMin;
    private double xMax;
    private double yMax;
    private double zMax;

    public CuboidRegion(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public CuboidRegion(CuboidParser parser) {
        this.xMin = parser.getXMin();
        this.yMin = parser.getYMin();
        this.zMin = parser.getZMin();
        this.xMax = parser.getXMax();
        this.yMax = parser.getYMax();
        this.zMax = parser.getZMax();

    }

    public double getXMin() {
        return xMin;
    }

    public double getYMin() {
        return yMin;
    }

    public double getZMin() {
        return zMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMax() {
        return yMax;
    }

    public double getZMax() {
        return zMax;
    }


    @Override
    public boolean contains(BlockRegion region) {
        return NumUtils.checkInterval(region.getX(), xMin, xMax) && NumUtils.checkInterval(region.getY(), yMin, yMax) && NumUtils.checkInterval(region.getZ(), zMin, zMax);

    }

    @Override
    public boolean contains(PointRegion region) {
        return NumUtils.checkInterval(region.getX(), xMin, xMax) && NumUtils.checkInterval(region.getY(), yMin, yMax) && NumUtils.checkInterval(region.getZ(), zMin, zMax);

    }

    @Override
    public PointRegion getRandomPoint() {
        return new PointRegion(NumUtils.randomInterval(xMin, xMax), NumUtils.randomInterval(yMin, yMax), NumUtils.randomInterval(zMin, zMax));
    }


}
