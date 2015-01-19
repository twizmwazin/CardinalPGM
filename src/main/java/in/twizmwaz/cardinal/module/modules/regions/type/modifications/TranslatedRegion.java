package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class TranslatedRegion extends RegionModule {

    private final RegionModule base;
    private final double xOffset, yOffset, zOffset;

    public TranslatedRegion(String name, RegionModule base, double xOffset, double yOffset, double zOffset) {
        super(name);
        this.base = base;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public TranslatedRegion(TranslateParser parser) {
        this(parser.getName(), parser.getBase(), parser.getXOffset(), parser.getYOffset(), parser.getZOffset());
    }

    public RegionModule getBase() {
        return base;
    }

    public double getXOffset() {
        return xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public double getZOffset() {
        return zOffset;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return base.contains(new BlockRegion(null, region.getX() - xOffset, region.getY() - yOffset, region.getZ() - zOffset));
    }

    @Override
    public boolean contains(PointRegion point) {
        return base.contains(new BlockRegion(null, point.getX() - xOffset, point.getY() - yOffset, point.getZ() - zOffset));
    }

    @Override
    public PointRegion getRandomPoint() {
        BlockRegion baseRandom = base.getCenterBlock();
        return new PointRegion(null, baseRandom.getX() + xOffset, baseRandom.getY() + yOffset, baseRandom.getZ() + zOffset);
    }

    @Override
    public BlockRegion getCenterBlock() {
        BlockRegion baseCenter = base.getCenterBlock();
        return new BlockRegion(null, baseCenter.getX() + xOffset, baseCenter.getY() + yOffset, baseCenter.getZ() + zOffset);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (Block block : getBase().getBlocks()) {
            results.add(new BlockRegion(null, block.getX() - 2 * getXOffset(), block.getY() - 2 * getYOffset(), block.getZ() - 2 * getZOffset()).getBlock());
        }
        return results;
    }
}
