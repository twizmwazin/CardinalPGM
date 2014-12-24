package in.twizmwaz.cardinal.regions.type.modifications;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.PointRegion;

public class MirroredRegion extends Region {

    private final Region base;
    private final double xOrigin, yOrigin, zOrigin;

    public MirroredRegion(Region base, double xOrigin, double yOrigin, double zOrigin) {
        this.base = base;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.zOrigin = zOrigin;
    }

    public MirroredRegion(MirrorParser parser) {
        this.base = parser.getBase();
        this.xOrigin = parser.getXOrigin();
        this.yOrigin = parser.getYOrigin();
        this.zOrigin = parser.getZOrigin();
    }

    public Region getBase() {
        return base;
    }

    public double getXOrigin() {
        return xOrigin;
    }

    public double getYOrigin() {
        return yOrigin;
    }

    public double getZOrigin() {
        return zOrigin;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return base.contains(new BlockRegion(2 * region.getX() - region.getX(), 2 * region.getY() - region.getY(), 2 * region.getZ() - region.getZ()));
    }

    @Override
    public boolean contains(PointRegion point) {
        return base.contains(new BlockRegion(2 * point.getX() - point.getX(), 2 * point.getY() - point.getY(), 2 * point.getZ() - point.getZ()));
    }

    @Override
    public PointRegion getRandomPoint() {
        PointRegion basePoint = base.getRandomPoint();
        return new PointRegion(2 * basePoint.getX() - basePoint.getX(), 2 * basePoint.getY() - basePoint.getY(), 2 * basePoint.getZ() - basePoint.getZ());
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(2 * base.getCenterBlock().getX() - base.getCenterBlock().getX(), 2 * base.getCenterBlock().getY() - base.getCenterBlock().getY(), 2 * base.getCenterBlock().getZ() - base.getCenterBlock().getZ());
    }
}
