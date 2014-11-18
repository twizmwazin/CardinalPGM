package in.twizmwaz.cardinal.regions.type.combinations;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;

import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public class UnionRegion extends Region {

    List<Region> regions;

    public UnionRegion(String name, List<Region> regions) {
        super(name);
        this.regions.addAll(regions);
    }

    public UnionRegion(CombinationParser parser) {
        super(parser.getName());
        this.regions.addAll(parser.getRegions());
    }

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(BlockRegion region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return true;
        }
        return false;
    }
}
