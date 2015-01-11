package in.twizmwaz.cardinal.regions.type;

import in.parapengu.commons.utils.OtherUtil;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.CuboidParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

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
        double x = (xMin > xMax) ? OtherUtil.getRandom(xMax, xMin) : OtherUtil.getRandom(xMin, xMax);
        double y = (yMin > yMax) ? OtherUtil.getRandom(yMax, yMin) : OtherUtil.getRandom(yMin, yMax);
        double z = (zMin > zMax) ? OtherUtil.getRandom(zMax, zMin) : OtherUtil.getRandom(zMin, zMax);
        return new PointRegion(x, y, z);
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(this.xMax - this.xMin, this.yMax - yMin, this.zMax - this.zMin);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (int x = (int) getXMin(); x <= getXMax(); x++) {
            for (int z = (int) getZMin(); z <= getZMax(); z++) {
                for (int y = (int) getYMin(); y <= getXMax(); y++) {
                    results.add((new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z).getBlock()));
                }
            }
        }
        return results;
    }


}
