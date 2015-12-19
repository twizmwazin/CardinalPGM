package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.EverywhereParser;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EverywhereRegion extends RegionModule {

    public EverywhereRegion(String name) {
        super(name);
    }

    public EverywhereRegion(EverywhereParser parser) {
        this(parser.getName());
    }

    @Override
    public boolean contains(Vector vector) {
        return true;
    }

    @Override
    public PointRegion getRandomPoint() {
        return null;
    }

    @Override
    public BlockRegion getCenterBlock() {
        return null;
    }

    @Override
    public List<Block> getBlocks() {
        return new ArrayList<Block>();
    }


}