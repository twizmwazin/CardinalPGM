package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.Region;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CuboidParser;
import in.twizmwaz.cardinal.util.NumUtils;

/**
 * Created by kevin on 10/26/14.
 */
public class CuboidRegion extends Region {

    private String name;
    private double xMin;
    private double yMin;
    private double zMin;
    private double xMax;
    private double yMax;
    private double zMax;

    public CuboidRegion(String name, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        super(name);
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public CuboidRegion(CuboidParser parser) {
        super(parser.getName());
        this.xMin = parser.getXMin();
        this.yMin = parser.getYMin();
        this.zMin = parser.getZMin();
        this.xMax = parser.getXMax();
        this.yMax = parser.getYMax();
        this.zMax = parser.getZMax();

    }

    public String getName() {
        return super.getName();
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
}
