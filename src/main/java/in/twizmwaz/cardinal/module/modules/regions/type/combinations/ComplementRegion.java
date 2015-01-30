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
    public boolean contains(BlockRegion region) {
        if (!regions.get(0).contains(region)) return false;
        for (int i = 1; i < regions.size(); i ++) {
            if (regions.get(i).contains(region)) return false;
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
}
