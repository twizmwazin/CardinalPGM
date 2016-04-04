package in.twizmwaz.cardinal.module.modules.regions.type.combinations;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntersectRegion extends RegionModule {

    private final ModuleCollection<RegionModule> regions;

    public IntersectRegion(String name, ModuleCollection<RegionModule> regions) {
        super(name);
        this.regions = regions;
    }

    public IntersectRegion(CombinationParser parser) {
        this(parser.getName(), parser.getRegions());
    }

    public ModuleCollection<RegionModule> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(Vector vector) {
        for (int i = 0; i < regions.size(); i++) {
            for (int k = 0; k < regions.size(); k++) {
                if (i != k) {
                    if (regions.get(i).contains(vector) && regions.get(k).contains(vector)) return true;
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
        for (RegionModule child : regions) {
            xTotal = xTotal + child.getCenterBlock().getX();
            yTotal = yTotal + child.getCenterBlock().getY();
            zTotal = zTotal + child.getCenterBlock().getZ();
        }
        return new BlockRegion(null, xTotal / regions.size(), yTotal / regions.size(), zTotal / regions.size());
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        try {
            for (int i = 0; i < regions.size(); i++) {
                for (int k = 0; k < regions.size(); k++) {
                    if (i != k) {
                        for (Block block : regions.get(i).getBlocks()) {
                            if (regions.get(k).contains(new BlockRegion(null, block.getLocation().toVector())) && !results.contains(block)) {
                                results.add(block);
                            }
                        }
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return results;
    }

    @Override
    public Vector getMin() {
        Vector min = regions.get(0).getMin();
        for (RegionModule region : regions) {
            Vector min2 = region.getMin();
            if (min2.getX() < min.getX()) min.setX(min2.getX());
            if (min2.getY() < min.getY()) min.setY(min2.getY());
            if (min2.getZ() < min.getZ()) min.setZ(min2.getZ());
        }
        return min;
    }

    @Override
    public Vector getMax() {
        Vector max = regions.get(0).getMax();
        for (RegionModule region : regions) {
            Vector max2 = region.getMax();
            if (max2.getX() > max.getX()) max.setX(max2.getX());
            if (max2.getY() > max.getY()) max.setY(max2.getY());
            if (max2.getZ() > max.getZ()) max.setZ(max2.getZ());
        }
        return max;
    }

}
