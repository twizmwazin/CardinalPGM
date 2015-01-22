package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

//This technically isn't accurate, although it probably works most of the time
public class MirroredRegion extends RegionModule {

    private final RegionModule base;
    private final Vector normal;

    public MirroredRegion(String name, RegionModule base, Vector normal) {
        super(name);
        this.base = base;
        this.normal = normal;
    }

    public MirroredRegion(MirrorParser parser) {
        this(parser.getName(), parser.getBase(), parser.getNormal());
    }

    public RegionModule getBase() {
        return base;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public boolean contains(BlockRegion region) {
        return base.contains(new BlockRegion(null, 
                normal.getBlockX() == 0 ? region.getX() - normal.getBlockX() : region.getX() ,
                normal.getBlockY() == 0 ? region.getY() - normal.getBlockY() : region.getY(),
                normal.getBlockZ() == 0 ? region.getZ() - normal.getBlockZ() : region.getZ()));
    }

    @Override
    public boolean contains(PointRegion point) {
        return base.contains(new BlockRegion(null,
                normal.getBlockX() == 0 ? point.getX() - normal.getBlockX() : point.getX() ,
                normal.getBlockY() == 0 ? point.getY() - normal.getBlockY() : point.getY(),
                normal.getBlockZ() == 0 ? point.getZ() - normal.getBlockZ() : point.getZ()));
    }

    @Override
    public PointRegion getRandomPoint() {
        PointRegion basePoint = base.getRandomPoint();
        return new PointRegion(null,
                normal.getBlockX() == 0 ? basePoint.getX() - normal.getBlockX() : basePoint.getX() ,
                normal.getBlockY() == 0 ? basePoint.getY() - normal.getBlockY() : basePoint.getY(),
                normal.getBlockZ() == 0 ? basePoint.getZ() - normal.getBlockZ() : basePoint.getZ());
    }

    @Override
    public BlockRegion getCenterBlock() {
        BlockRegion basePoint = base.getCenterBlock();
        return new PointRegion(null,
                normal.getBlockX() == 0 ? basePoint.getX() - normal.getBlockX() : basePoint.getX() ,
                normal.getBlockY() == 0 ? basePoint.getY() - normal.getBlockY() : basePoint.getY(),
                normal.getBlockZ() == 0 ? basePoint.getZ() - normal.getBlockZ() : basePoint.getZ());
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        for (Block block : getBase().getBlocks()) {
            results.add(new BlockRegion(null,
                    normal.getBlockX() == 0 ? block.getX() - normal.getBlockX() : block.getX() ,
                    normal.getBlockY() == 0 ? block.getY() - normal.getBlockY() : block.getY(),
                    normal.getBlockZ() == 0 ? block.getZ() - normal.getBlockZ() : block.getZ()).getBlock());
        }
        return results;
    }
}
