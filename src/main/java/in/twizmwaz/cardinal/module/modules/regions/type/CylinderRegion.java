package in.twizmwaz.cardinal.module.modules.regions.type;

import in.parapengu.commons.utils.OtherUtil;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CylinderParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CylinderRegion extends RegionModule {

    private final double baseX, baseY, baseZ, radius, height;
    
    public CylinderRegion(String name, double baseX, double baseY, double baseZ, double radius, double height) {
        super(name);
        this.baseX = baseX;
        this.baseY = baseY;
        this.baseZ = baseZ;
        this.radius = radius;
        this.height = height;
    }

    public CylinderRegion(CylinderParser parser) {
        this(parser.getName(), parser.getBaseX(), parser.getBaseY(), parser.getBaseZ(), parser.getRadius(), parser.getHeight());
    }

    public double getBaseX() {
        return baseX;
    }

    public double getBaseY() {
        return baseY;
    }

    public double getBaseZ() {
        return baseZ;
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
    public boolean contains(PointRegion region) {
        return (Math.hypot(Math.abs(region.getX() - getBaseX()), Math.abs(region.getZ() - getBaseZ())) <= getRadius()) && NumUtils.checkInterval(region.getY(), getBaseY(), getBaseY() + getHeight());

    }

    @Override
    public PointRegion getRandomPoint() {
        double a = OtherUtil.getRandom(0, radius);
        double b = OtherUtil.getRandom(0, 360);
        double c = OtherUtil.getRandom(0, height);

        return new PointRegion(null, this.baseX + a * Math.cos(b), this.baseY + c, this.baseZ + a * Math.sin(b));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return (new BlockRegion(null, this.baseX, this.baseY + .5 * height, this.baseZ));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, baseX - radius, baseY, baseZ - radius, baseX + radius, baseY + height, baseZ + radius);
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }
}
