package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CuboidRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import in.twizmwaz.cardinal.util.FlooredVector;
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
    public boolean contains(Vector vector) {
        if (this.region == null) {
            updateRegion();
        }
        return this.region.contains(vector);
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
        for (Block block : base.getBlocks()) {
            int x = 0;
            int y = 0;
            int z = 0;
            if (normal.getX() != 0) {
                x = (int) Math.floor((block.getX() - origin.getX()) * -2.0);
            }
            if (normal.getY() != 0) {
                y = (int) Math.floor((block.getY() - origin.getY()) * -2.0);
            }
            if (normal.getZ() != 0) {
                z = (int) Math.floor((block.getZ() - origin.getZ()) * -2.0);
            }
            blocks.add(new CuboidRegion(null, block.getX() + x, block.getY() + y, block.getZ() + z, block.getX() + x, block.getY() + y, block.getZ() + z));
        }
        this.region = new UnionRegion(null, blocks);
    }
}
