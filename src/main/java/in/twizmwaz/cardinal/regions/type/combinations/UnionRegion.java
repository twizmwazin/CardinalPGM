package in.twizmwaz.cardinal.regions.type.combinations;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.WorldPoint;

import java.util.List;

/**
 * Created by kevin on 10/26/14.
 */
public class UnionRegion extends Region {

    List<Region> regions;

    public UnionRegion(String name, List<Region> regions) {
        this.regions.addAll(regions);
    }

    public UnionRegion(CombinationParser parser) {
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

    @Override
    public boolean contains(WorldPoint region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return true;
        }
        return false;
    }

    @Override
    public WorldPoint getRandomPoint() {
        while (true) {
            WorldPoint point = regions.get(0).getRandomPoint();
            int success = 0;
            for (Region region : regions) {
                if (region.contains(point)) {
                    success++;
                }
            }
            if (success == regions.size()) {
                return point;
            }
        }

    }
}
