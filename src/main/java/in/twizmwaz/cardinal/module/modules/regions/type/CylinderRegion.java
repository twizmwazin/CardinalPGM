package in.twizmwaz.cardinal.module.modules.regions.type;

import in.parapengu.commons.utils.OtherUtil;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CylinderParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CylinderRegion extends RegionModule {

    private final Vector base;
    private final double radius, height;
    
    public CylinderRegion(String name, Vector base, double radius, double height) {
        super(name);
        this.base = base;
        this.radius = radius;
        this.height = height;
    }

    public CylinderRegion(CylinderParser parser) {
        this(parser.getName(), parser.getBase(), parser.getRadius(), parser.getHeight());
    }

    public double getBaseX() {
        return base.getX();
    }

    public double getBaseY() {
        return base.getY();
    }

    public double getBaseZ() {
        return base.getZ();
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return (Math.hypot(Math.abs(region.getX() - getBaseX()), Math.abs(region.getZ() - getBaseZ())) <= getRadius()) && NumUtils.checkInterval(region.getY(), getBaseY(), getBaseY() + getHeight());
    }

    @Override
    public PointRegion getRandomPoint() {
        double a = OtherUtil.getRandom(0, radius);
        double b = OtherUtil.getRandom(0, 360);
        double c = OtherUtil.getRandom(0, height);

        return new PointRegion(null, getBaseX() + a * Math.cos(b), getBaseY() + c, getBaseZ() + a * Math.sin(b));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return (new BlockRegion(null, new BlockVector(getBaseX(), getBaseY() + .5 * height, getBaseZ())));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getBaseX() - radius, getBaseY(), getBaseZ() - radius, getBaseX() + radius, getBaseY() + height, getBaseZ() + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
