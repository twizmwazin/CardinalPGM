package in.twizmwaz.cardinal.module.modules.regions.type.combinations;


import in.twizmwaz.cardinal.module.modules.regions.Region;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;

import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public class ComplementRegion extends Region {

    private List<Region> regions;

    public ComplementRegion(String name, List<Region> regions) {
        super(name);
        this.regions.addAll(regions);
    }

    public ComplementRegion(CombinationParser parser) {
        super(parser.getName());
        this.regions.addAll(parser.getRegions());

    }

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(BlockRegion region) {
        List<Region> working = getRegions();
        for (Region work : working) {
            if (work.contains(region)) {
                working.remove(work);
            }
        }
        if (working.size() == 1) return true;
        else return false;
    }
}
