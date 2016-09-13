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

public class NegativeRegion extends RegionModule {

    private final ModuleCollection<RegionModule> regions;

    public NegativeRegion(String name, ModuleCollection<RegionModule> regions) {
        super(name);
        this.regions = regions;
    }

    public NegativeRegion(CombinationParser parser) {
        this(parser.getName(), parser.getRegions());
    }

    public List<RegionModule> getRegions() {
        return regions;
    }

    @Override
    public boolean contains(Vector vector) {
        for (RegionModule reg : getRegions()) {
            if (reg.contains(vector)) return false;
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
        //I'm aware this is pretty much awful. Anyone feel free to make a pull request if they have any good way of going about this.
        return new ArrayList<Block>();
    }

    @Override
    public Vector getMin() {
        Vector min = new Vector(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (RegionModule region : regions) {
            min.minimize(region.getMin());
        }
        return min;
    }

    @Override
    public Vector getMax() {
        Vector max = new Vector(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (RegionModule region : regions) {
            max.maximize(region.getMax());
        }
        return max;
    }

}
