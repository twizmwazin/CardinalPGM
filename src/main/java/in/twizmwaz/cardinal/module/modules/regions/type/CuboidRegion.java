package in.twizmwaz.cardinal.module.modules.regions.type;

import in.parapengu.commons.utils.OtherUtil;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CuboidParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CuboidRegion extends RegionModule {

    private final Vector min, max;

    public CuboidRegion(String name, Vector min, Vector max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    public CuboidRegion(String name, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        this(name, new Vector(xMin, yMin, zMin), new Vector(xMax, yMax, zMax));
    }

    public CuboidRegion(CuboidParser parser) {
        this(parser.getName(), parser.getMin(), parser.getMax());
    }

    public double getXMin() {
        return min.getX();
    }

    public double getYMin() {
        return min.getY();
    }

    public double getZMin() {
        return min.getZ();
    }

    public double getXMax() {
        return max.getX();
    }

    public double getYMax() {
        return max.getY();
    }

    public double getZMax() {
        return max.getZ();
    }


    @Override
    public boolean contains(BlockRegion region) {
        return region.getVector().isInAABB(min, max);
    }

    @Override
    public boolean contains(PointRegion region) {
        return region.getVector().isInAABB(min, max);
    }

    @Override
    public PointRegion getRandomPoint() {
        double x = (min.getX() > max.getX()) ? OtherUtil.getRandom(max.getX(), min.getY()) : OtherUtil.getRandom(min.getX(), max.getX());
        double y = (min.getY() > max.getBlockY()) ? OtherUtil.getRandom(max.getY(), min.getY()) : OtherUtil.getRandom(min.getY(), max.getY());
        double z = (min.getZ() > max.getZ()) ? OtherUtil.getRandom(max.getZ(), min.getZ()) : OtherUtil.getRandom(min.getZ(), max.getZ());
        return new PointRegion(null, x, y, z);
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, min.getMidpoint(max));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (int x = (int) getXMin(); x <= getXMax(); x++) {
            for (int z = (int) getZMin(); z <= getZMax(); z++) {
                for (int y = (int) getYMin(); y <= getYMax(); y++) {
                    results.add((new Location(GameHandler.getGameHandler().getMatchWorld(), x, y, z).getBlock()));
                }
            }
        }
        return results;
    }


}
