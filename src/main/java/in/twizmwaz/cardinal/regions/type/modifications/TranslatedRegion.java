package in.twizmwaz.cardinal.regions.type.modifications;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.PointRegion;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class TranslatedRegion extends Region {

    private final Region base;
    private final double xOffset, yOffset, zOffset;

    public TranslatedRegion(Region base, double xOffset, double yOffset, double zOffset) {
        this.base = base;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public TranslatedRegion(TranslateParser parser) {
        this.base = parser.getBase();
        this.xOffset = parser.getXOffset();
        this.yOffset = parser.getYOffset();
        this.zOffset = parser.getZOffset();
    }

    public Region getBase() {
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
        return base.contains(new BlockRegion(region.getX() - xOffset, region.getY() - yOffset, region.getZ() - zOffset));
    }

    @Override
    public boolean contains(PointRegion point) {
        return base.contains(new BlockRegion(point.getX() - xOffset, point.getY() - yOffset, point.getZ() - zOffset));
    }

    @Override
    public PointRegion getRandomPoint() {
        BlockRegion baseRandom = base.getCenterBlock();
        return new PointRegion(baseRandom.getX() + xOffset, baseRandom.getY() + yOffset, baseRandom.getZ() + zOffset);
    }

    @Override
    public BlockRegion getCenterBlock() {
        BlockRegion baseCenter = base.getCenterBlock();
        return new BlockRegion(baseCenter.getX() + xOffset, baseCenter.getY() + yOffset, baseCenter.getZ() + zOffset);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (Block block : getBase().getBlocks()) {
            results.add(new BlockRegion(block.getX() - 2 * getXOffset(), block.getY() - 2 * getYOffset(), block.getZ() - 2 * getZOffset()).getBlock());
        }
        return results;
    }
}
