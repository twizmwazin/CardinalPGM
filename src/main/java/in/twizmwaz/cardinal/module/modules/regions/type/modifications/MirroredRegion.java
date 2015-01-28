package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule base;
    private final RegionModule region;
    private final Vector origin, normal;

    public MirroredRegion(String name, RegionModule base, Vector origin, Vector normal) {
        super(name);
        this.base = base;
        this.origin = origin;
        this.normal = normal;

        ModuleCollection<RegionModule> blocks = new ModuleCollection<>();
//        Bukkit.broadcastMessage("getBlocks() called");
        Vector translation = new Vector(origin.getBlockX() * normal.getBlockX(), origin.getBlockY() * normal.getBlockY(), origin.getBlockZ() * normal.getBlockZ());
//        Bukkit.broadcastMessage("translation: " + translation.getX() + ", " + translation.getY() + ", " + translation.getZ());
//        Bukkit.broadcastMessage(base.getBlocks().size() + "");
        for (Block block : base.getBlocks()) {
//            Bukkit.broadcastMessage("block: " + block.getX() + ", " + block.getY() + ", " + block.getZ());
            Block newBlock = block.getRelative(translation.getBlockX() * -1, translation.getBlockY() * -1, translation.getBlockZ() * -1);
//            Bukkit.broadcastMessage("newBlock: " + newBlock.getX() + ", " + newBlock.getY() + ", " + newBlock.getZ());
            blocks.add(new BlockRegion(null, newBlock.getX(), newBlock.getY(), newBlock.getZ()));
        }

        region = new UnionRegion(null, blocks);
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
        return this.region.getBlocks().contains(region.getBlock());
    }

    @Override
    public PointRegion getRandomPoint() {
        return region.getRandomPoint();
    }

    @Override
    public BlockRegion getCenterBlock() {
        return region.getCenterBlock();
    }

    @Override
    public List<Block> getBlocks() {
        return region.getBlocks();
    }
}
