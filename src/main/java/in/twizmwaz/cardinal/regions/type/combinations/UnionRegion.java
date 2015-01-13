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
public class UnionRegion extends Region {

    private List<Region> regions;

    public UnionRegion(String name, List<Region> regions) {
        regions = new ArrayList<>();
        this.regions.addAll(regions);
    }

    public UnionRegion(List<Region> regions) {
        this.regions = regions;
    }

    public UnionRegion(CombinationParser parser) {
        regions = new ArrayList<>();
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
    public boolean contains(PointRegion region) {
        for (Region reg : getRegions()) {
            if (reg.contains(region)) return true;
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

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (Region region : regions) {
            results.addAll(region.getBlocks());
        }
        return results;
    }
}
