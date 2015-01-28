package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule base;
    private RegionModule region;
    private final Vector origin, normal;

    public MirroredRegion(String name, RegionModule base, Vector origin, Vector normal) {
        super(name);
        this.base = base;
        this.origin = origin;
        this.normal = normal;
        this.region = null;
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
        if (this.region == null) {
            updateRegion();
        }
        return this.region.contains(region);
    }

    @Override
    public PointRegion getRandomPoint() {
        if (this.region == null) {
            updateRegion();
        }
        return this.region.getRandomPoint();
    }

    @Override
    public BlockRegion getCenterBlock() {
        if (this.region == null) {
            updateRegion();
        }
        return this.region.getCenterBlock();
    }

    @Override
    public List<Block> getBlocks() {
        if (this.region == null) {
            updateRegion();
        }
        return this.region.getBlocks();
    }

    public void updateRegion() {
        ModuleCollection<RegionModule> blocks = new ModuleCollection<>();
        Vector translation = new Vector(origin.getBlockX() * (normal.getBlockX() != 0 ? 1: 0), origin.getBlockY() * (normal.getBlockY() != 0 ? 1: 0), origin.getBlockZ() * (normal.getBlockZ() != 0 ? 1: 0));
        for (Block block : base.getBlocks()) {
            Location location = block.getLocation();
            int xRelative = 0;
            int yRelative = 0;
            int zRelative = 0;
            if (normal.getBlockX() != 0) {
                xRelative = (location.getBlockX() - translation.getBlockX()) * -2;
            }
            if (normal.getBlockY() != 0) {
                yRelative = (location.getBlockY() - translation.getBlockY()) * -2;
            }
            if (normal.getBlockZ() != 0) {
                zRelative = (location.getBlockZ() - translation.getBlockZ()) * -2;
            }
            Block newBlock = block.getRelative(xRelative, yRelative, zRelative);
            blocks.add(new BlockRegion(null, newBlock.getX(), newBlock.getY(), newBlock.getZ()));
        }
        this.region = new UnionRegion(null, blocks);
    }
}
