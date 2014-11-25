package in.twizmwaz.cardinal.regions.type.combinations;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.point.PointRegion;
import in.twizmwaz.cardinal.regions.type.BlockRegion;

import java.util.List;
import java.util.Random;

/**
 * Created by kevin on 10/26/14.
 */
public class NegativeRegion extends Region {

    List<Region> regions;

    public NegativeRegion(String name, List<Region> regions) {
        this.regions.addAll(regions);
    }

    public NegativeRegion(CombinationParser parser) {
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

    @Override
    public boolean contains(PointRegion region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return false;
        }
        return true;
    }

    @Override
    public PointRegion getRandomPoint() {
        while (true) {
            Random random = new Random();
            PointRegion point = regions.get(random.nextInt(regions.size())).getRandomPoint();
            if (this.contains(point)) {
                return point;
            }
        }
    }
}
