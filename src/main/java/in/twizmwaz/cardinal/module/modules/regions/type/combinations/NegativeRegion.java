package in.twizmwaz.cardinal.module.modules.regions.type.combinations;

import in.twizmwaz.cardinal.module.modules.regions.Region;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;

import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public class NegativeRegion extends Region {

    List<Region> regions;

    public NegativeRegion(String name, List<Region> regions) {
        super(name);
        this.regions.addAll(regions);
    }

    public NegativeRegion(CombinationParser parser) {
        super(parser.getName());
        this.regions.addAll(parser.getRegions());

    }

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(BlockRegion region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return false;
        }
        return true;
    }
}
