package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule base;
    private final double xOrigin, yOrigin, zOrigin;

    public MirroredRegion(String name, RegionModule base, double xOrigin, double yOrigin, double zOrigin) {
        super(name);
        this.base = base;
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.zOrigin = zOrigin;
    }

    public MirroredRegion(MirrorParser parser) {
        this(parser.getName(), parser.getBase(), parser.getXOrigin(), parser.getYOrigin(), parser.getZOrigin());
    }

    public RegionModule getBase() {
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
        return base.contains(new BlockRegion(null, 2 * region.getX() - region.getX(), 2 * region.getY() - region.getY(), 2 * region.getZ() - region.getZ()));
    }

    @Override
    public boolean contains(PointRegion point) {
        return base.contains(new BlockRegion(null, 2 * point.getX() - point.getX(), 2 * point.getY() - point.getY(), 2 * point.getZ() - point.getZ()));
    }

    @Override
    public PointRegion getRandomPoint() {
        PointRegion basePoint = base.getRandomPoint();
        return new PointRegion(null, 2 * basePoint.getX() - basePoint.getX(), 2 * basePoint.getY() - basePoint.getY(), 2 * basePoint.getZ() - basePoint.getZ());
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, 2 * base.getCenterBlock().getX() - base.getCenterBlock().getX(), 2 * base.getCenterBlock().getY() - base.getCenterBlock().getY(), 2 * base.getCenterBlock().getZ() - base.getCenterBlock().getZ());
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (Block block : getBase().getBlocks()) {
            results.add(new BlockRegion(null, block.getX() - 2 * getXOrigin(), block.getY() - 2 * getYOrigin(), block.getZ() - 2 * getZOrigin()).getBlock());
        }
        return results;
    }
}
