package in.twizmwaz.cardinal.regions.type;


import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.CylinderParser;
import in.twizmwaz.cardinal.util.NumUtils;

public class CylinderRegion extends Region {

    private String name;
    private double baseX;
    private double baseY;
    private double baseZ;
    private double radius;
    private double height;

    public CylinderRegion(String name, double baseX, double baseY, double baseZ, double radius, double height) {
        super(name);
        this.name = name;
        this.baseX = baseX;
        this.baseY = baseY;
        this.baseZ = baseZ;
        this.radius = radius;
        this.height = height;
    }

    public CylinderRegion(CylinderParser parser) {
        super(parser.getName());
        this.name = parser.getName();
        this.baseX = parser.getBaseX();
        this.baseY = parser.getBaseY();
        this.baseZ = parser.getBaseZ();
    }

    public String getName() {
        return super.getName();
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
}
