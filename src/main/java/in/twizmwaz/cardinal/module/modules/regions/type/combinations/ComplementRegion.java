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

public class ComplementRegion extends RegionModule {

    private ModuleCollection<RegionModule> regions;

    public ComplementRegion(String name, ModuleCollection<RegionModule> regions) {
        super(name);
        this.regions = regions;
    }

    public ComplementRegion(CombinationParser parser) {
        this(parser.getName(), parser.getRegions());
    }

    public ModuleCollection<RegionModule> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(Vector vector) {
        if (!regions.get(0).contains(vector)) return false;
        for (int i = 1; i < regions.size(); i++) {
            if (regions.get(i).contains(vector)) return false;
        }
        return true;
    }

    @Override
    public PointRegion getRandomPoint() {
        while (true) {
            Random random = new Random();
            PointRegion point = regions.get(0).getRandomPoint();
            if (this.contains(point)) {
                return point;
            }
        }
    }

    @Override
    public BlockRegion getCenterBlock() {
        return regions.get(0).getCenterBlock();
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
