package in.twizmwaz.cardinal.module.modules.regions;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;

import java.util.List;

public abstract class RegionModule implements Module {
    
    protected String name;
    
    public RegionModule(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract boolean contains(BlockRegion region);

    public abstract boolean contains(PointRegion point);

    public abstract PointRegion getRandomPoint();

    public abstract BlockRegion getCenterBlock();

    public abstract List<Block> getBlocks();
    
    @Override
    public void unload(){
    }

}
