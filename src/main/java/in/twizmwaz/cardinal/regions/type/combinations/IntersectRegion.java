package in.twizmwaz.cardinal.regions.type.combinations;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.WorldPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public class IntersectRegion extends Region {

    List<Region> regions;

    public IntersectRegion(List<Region> regions) {
        this.regions.addAll(regions);
    }

    public IntersectRegion(CombinationParser parser) {
        this.regions.addAll(parser.getRegions());
    }

    public List<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(BlockRegion region) {
        for (Region reg : getRegions()) {
            List<Region> working = new ArrayList<Region>();
            working.addAll(getRegions());
            if (reg.contains(region)) {
                working.remove(region);
                for (Region work : working) {
                    if (work.contains(region)) return true;

                }

            }

        }
        return false;
    }

    @Override
    public boolean contains(WorldPoint region) {
        for (Region reg : getRegions()) {
            List<Region> working = new ArrayList<Region>();
            working.addAll(getRegions());
            if (reg.contains(region)) {
                working.remove(region);
                for (Region work : working) {
                    if (work.contains(region)) return true;

                }

            }

        }
        return false;
    }

    @Override
    public WorldPoint getRandomPoint() {
        while (true) {
            WorldPoint point = regions.get(0).getRandomPoint();
            for (Region reg : getRegions()) {
                List<Region> working = new ArrayList<Region>();
                working.addAll(getRegions());
                if (reg.contains(point)) {
                    working.remove(point);
                    for (Region work : working) {
                        if (work.contains(point)) return point;

                    }

                }

            }
        }
    }
}
