package in.twizmwaz.cardinal.module.modules.regions.type;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.EmptyParser;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EmptyRegion extends RegionModule {

    public EmptyRegion(String name) {
        super(name);
    }

    public EmptyRegion(EmptyParser parser) {
        this(parser.getName());
    }

    @Override
    public boolean contains(Vector vector) {
        return false;
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

    @Override
    public Vector getMin() {
        return new Vector(0,0,0);
    }

    @Override
    public Vector getMax() {
        return new Vector(0,0,0);
    }

}
