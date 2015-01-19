package in.twizmwaz.cardinal.module.modules.regions.type.combinations;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;

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
    public boolean contains(BlockRegion region) {
        for (RegionModule reg : getRegions()) {
            ModuleCollection<RegionModule> working = new ModuleCollection<>();
            working.addAll(getRegions());
            if (reg.contains(region)) {
                working.remove(region);
                for (RegionModule work : working) {
                    if (work.contains(region)) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(PointRegion region) {
        for (RegionModule reg : getRegions()) {
            ModuleCollection<RegionModule> working = new ModuleCollection<RegionModule>();
            working.addAll(getRegions());
            if (reg.contains(region)) {
                working.remove(region);
                for (RegionModule work : working) {
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
            for (Block block : regions.get(0).getBlocks()) {
                if (contains(new BlockRegion(null, block.getX(), block.getY(), block.getZ()))) results.add(block);
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return results;
    }
}
