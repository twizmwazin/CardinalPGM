package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class TranslatedRegion extends RegionModule {

    private final Vector offset;
    private final RegionModule base;

    public TranslatedRegion(String name, RegionModule base, Vector offset) {
        super(name);
        this.offset = offset;
        this.base = base;
    }

    public TranslatedRegion(TranslateParser parser) {
        this(parser.getName(), parser.getBase(), parser.getOffset());
    }

    @Override
    public boolean contains(Vector vector) {
        return base.contains(vector.minus(offset));
    }

    @Override
    public PointRegion getRandomPoint() {
        return new PointRegion(null, base.getRandomPoint().getVector().plus(offset));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, base.getCenterBlock().getVector().plus(offset));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> result = Lists.newArrayList();
        World world = GameHandler.getGameHandler().getMatchWorld();
        for (Block block : base.getBlocks()) {
            result.add(block.getLocation().add(offset).toLocation(world).getBlock());
        }
        return result;
    }

    @Override
    public Vector getMin() {
        return base.getMin().plus(offset);
    }

    @Override
    public Vector getMax() {
        return base.getMax().plus(offset);
    }

}
