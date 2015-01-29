package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;
import org.bukkit.util.*;

import java.util.ArrayList;
import java.util.List;

public class TranslatedRegion extends RegionModule {

    private final RegionModule base;
    private final Vector offset;

    public TranslatedRegion(String name, RegionModule base, Vector offset) {
        super(name);
        this.base = base;
        this.offset = offset;
    }

    public TranslatedRegion(TranslateParser parser) {
        this(parser.getName(), parser.getBase(), parser.getOffset());
    }

    public RegionModule getBase() {
        return base;
    }

    public double getXOffset() {
        return offset.getX();
    }

    public double getYOffset() {
        return offset.getY();
    }

    public double getZOffset() {
        return offset.getZ();
    }

    public Vector getOffset() {
        return offset;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return base.contains(new BlockRegion(null, region.getVector().add(getOffset())));
    }

    @Override
    public PointRegion getRandomPoint() {
        BlockRegion baseRandom = base.getCenterBlock();
        return new PointRegion(null, baseRandom.getVector().add(getOffset()));
    }

    @Override
    public BlockRegion getCenterBlock() {
        BlockRegion baseCenter = base.getCenterBlock();
        return new BlockRegion(null, baseCenter.getVector().add(getOffset()));
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
