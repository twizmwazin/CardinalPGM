package in.twizmwaz.cardinal.regions.type.combinations;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.PointRegion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public boolean contains(PointRegion region) {
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
    public PointRegion getRandomPoint() {
        while (true) {
            Random random = new Random();
            PointRegion point = regions.get(random.nextInt(regions.size())).getRandomPoint();
            if (this.contains(point)) {
                return point;
            }
        }
    }

    @Override
    public BlockRegion getCenterBlock() {
        double xTotal = 0, yTotal = 0, zTotal = 0;
        for (Region child : regions) {
            xTotal = xTotal + child.getCenterBlock().getX();
            yTotal = yTotal + child.getCenterBlock().getY();
            zTotal = zTotal + child.getCenterBlock().getZ();
        }
        return new BlockRegion(xTotal / regions.size(), yTotal / regions.size(), zTotal / regions.size());
    }
}
