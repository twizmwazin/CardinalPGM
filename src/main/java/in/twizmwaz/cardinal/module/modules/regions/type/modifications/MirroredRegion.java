package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule base;
    private final Vector origin, normal;

    public MirroredRegion(String name, RegionModule base, Vector origin, Vector normal) {
        super(name);
        this.base = base;
        this.origin = origin;
        this.normal = normal;
    }

    public MirroredRegion(MirrorParser parser) {
        this(parser.getName(), parser.getBase(), parser.getOrigin(), parser.getNormal());
    }

    public RegionModule getBase() {
        return base;
    }

    public Vector getOrigin() {
        return origin;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public boolean contains(BlockRegion region) {
        Vector translation = getOrigin().multiply(2).multiply(getNormal());
        return base.contains(new BlockRegion(null, translation.subtract(region.getVector())));
    }

    @Override
    public PointRegion getRandomPoint() {
        PointRegion basePoint = base.getRandomPoint();
        Vector translation = getOrigin().multiply(2).multiply(getNormal());
        return new PointRegion(null, translation.subtract(basePoint.getVector()));
    }

    @Override
    public BlockRegion getCenterBlock() {
        BlockRegion basePoint = base.getCenterBlock();
        Vector translation = getOrigin().multiply(2).multiply(getNormal());
        return new PointRegion(null, translation.subtract(basePoint.getVector()));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> results = new ArrayList<>();
        Vector translation = getOrigin().multiply(2).multiply(getNormal());
        for (Block block : getBase().getBlocks()) {
            results.add(new BlockRegion(null, translation.subtract(block.getLocation().toVector())).getBlock());
        }
        return results;
    }
}
