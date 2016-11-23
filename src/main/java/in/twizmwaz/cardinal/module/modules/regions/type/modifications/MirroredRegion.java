package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule base;
    private final Vector origin, normal, flippedNormal;

    public MirroredRegion(String name, RegionModule base, Vector origin, Vector normal) {
        super(name);
        this.origin = origin;
        this.normal = normal.normalize();
        this.flippedNormal = new Vector(normal.getX() * -1, normal.getY() * -1, normal.getZ() * -1);
        this.base = base;
    }

    public MirroredRegion(MirrorParser parser) {
        this(parser.getName(), parser.getBase(), parser.getOrigin(), parser.getNormal());
    }

    @Override
    public boolean contains(Vector vector) {
        return base.contains(mirrorVector(vector, origin, flippedNormal));
    }

    @Override
    public PointRegion getRandomPoint() {
        return new PointRegion(null, mirrorVector(base.getRandomPoint().getVector(), origin, normal));
    }

    @Override
    public BlockRegion getCenterBlock() {
        return new BlockRegion(null, mirrorVector(base.getCenterBlock().getVector(), origin, normal));
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> result = Lists.newArrayList();
        World world = GameHandler.getGameHandler().getMatchWorld();
        for (Block block : base.getBlocks()) {
            result.add(mirrorVector(block.getLocation().position(), origin, normal).toLocation(world).getBlock());
        }
        return result;
    }

    @Override
    public Vector getMin() {
        return mirrorVector(base.getMin(), origin, normal);
    }

    @Override
    public Vector getMax() {
        return mirrorVector(base.getMax(), origin, normal);
    }

    public static Vector mirrorVector(Vector original, Vector origin, Vector normal) {
        Vector vector = original.minus(origin);
        vector = vector.minus(normal.times(vector.dot(normal)).times(2)).plus(origin);
        vector = new Vector(round(vector.getX()), round(vector.getY()), round(vector.getZ()));
        return vector;
    }

    public static double round(double d) {
        return (double) Math.round(d * 10) / 10D;
    }

}
