package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.CylinderParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.block.Block;
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
    public boolean contains(Vector vector) {
        return (Math.hypot(Math.abs(vector.getX() - getBaseX()), Math.abs(vector.getZ() - getBaseZ())) <= getRadius()) && Numbers.checkInterval(vector.getY(), getBaseY(), getBaseY() + getHeight());
    }

    @Override
    public PointRegion getRandomPoint() {
        double a = Numbers.getRandom(0, radius);
        double b = Numbers.getRandom(0, 360);
        double c = Numbers.getRandom(0, height);

        return new PointRegion(null, getBaseX() + a * Math.cos(b), getBaseY() + c, getBaseZ() + a * Math.sin(b));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return (new BlockRegion(null, new Vector(getBaseX(), getBaseY() + .5 * height, getBaseZ())));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        CuboidRegion bound = new CuboidRegion(null, getMin(), getMax());
        for (Block block : bound.getBlocks()) {
            if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
        }
        return results;
    }

    @Override
    public Vector getMin() {
        return new Vector(getBaseX() - radius, getBaseY(), getBaseZ() - radius);
    }

    @Override
    public Vector getMax() {
        return new Vector(getBaseX() + radius, getBaseY() + height, getBaseZ() + radius);
    }

}
