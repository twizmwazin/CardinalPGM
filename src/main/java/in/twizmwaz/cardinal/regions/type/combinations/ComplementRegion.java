package in.twizmwaz.cardinal.regions.type.combinations;


import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.PointRegion;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kevin on 10/26/14.
 */
public class ComplementRegion extends Region {

    private List<Region> regions;

    public ComplementRegion(String name, List<Region> regions) {
        this.regions.addAll(regions);
    }

    public ComplementRegion(CombinationParser parser) {
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

    @Override
    public boolean contains(PointRegion region) {
        List<Region> working = getRegions();
        for (Region work : working) {
            if (work.contains(region)) {
                working.remove(work);
            }
        }
        if (working.size() == 1) return true;
        else return false;
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

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        try {
            results.addAll(regions.get(0).getBlocks()); 
            for (int i = 1; i < regions.size(); i++) {
                results.removeAll(regions.get(i).getBlocks());
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return results;
    }
}
