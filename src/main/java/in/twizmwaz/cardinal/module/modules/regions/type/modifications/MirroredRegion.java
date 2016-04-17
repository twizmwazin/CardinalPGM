package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CircleRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CuboidRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.CylinderRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.EmptyRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.PointRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.RectangleRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.SphereRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule region;
    private final Vector origin, normal;

    public MirroredRegion(String name, RegionModule base, Vector origin, Vector normal) {
        super(name);
        this.origin = origin;
        this.normal = normal.normalize();
        this.region = mirrorRegion(base);
    }

    public MirroredRegion(String name, RegionModule region) {
        super(name);
        this.origin = null;
        this.normal = null;
        this.region = region;
    }

    public MirroredRegion(MirrorParser parser) {
        this(parser.getName(), parser.getBase(), parser.getOrigin(), parser.getNormal());
    }

    public RegionModule getRegion() {
        return region;
    }

    @Override
    public boolean contains(Vector vector) {
        return region.contains(vector);
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

    @Override
    public Vector getMin() {
        return region.getMin();
    }

    @Override
    public Vector getMax() {
        return region.getMax();
    }

    @SuppressWarnings({"unchecked"})
    private <T extends RegionModule> T mirrorRegion(T region) {


        if (region instanceof PointRegion) {

            PointRegion rg = (PointRegion) region;
            Vector v = getMirroredValues(rg.getX(), rg.getY(), rg.getZ());
            return (T) new PointRegion(null, v.getX(), v.getY(), v.getZ());

        } else if (region instanceof BlockRegion) {

            BlockRegion rg = (BlockRegion) region;
            Vector v = getMirroredValues(rg.getX(), rg.getY(), rg.getZ());
            return (T) new BlockRegion(null, v.getX(), v.getY(), v.getZ());

        } else if (region instanceof CircleRegion) {

            CircleRegion rg = (CircleRegion) region;
            Vector v = getMirroredValues(rg.getBaseX(), 0, rg.getBaseZ());
            return (T) new CircleRegion(null, v.getX(), v.getZ(), rg.getRadius());

        } else if (region instanceof CylinderRegion) {

            CylinderRegion rg = (CylinderRegion) region;
            Vector v = getMirroredValues(rg.getBaseX(), rg.getBaseY(), rg.getBaseZ());
            return (T) new CylinderRegion(null, v, rg.getRadius(), rg.getHeight());

        } else if (region instanceof RectangleRegion) {

            RectangleRegion rg = (RectangleRegion) region;
            Vector v1 = getMirroredValues(rg.getXMin(), 0, rg.getZMin());
            Vector v2 = getMirroredValues(rg.getXMax(), 0, rg.getZMax());
            return (T) new RectangleRegion(null, v1.getX(), v1.getZ(), v2.getX(), v2.getZ());

        } else if (region instanceof CuboidRegion) {

            CuboidRegion rg = (CuboidRegion) region;
            Vector v1 = getMirroredValues(rg.getXMin(), rg.getYMin(), rg.getZMin());
            Vector v2 = getMirroredValues(rg.getXMax(), rg.getYMax(), rg.getZMax());
            return (T) new CuboidRegion(null, v1, v2);

        } else if (region instanceof SphereRegion) {

            SphereRegion rg = (SphereRegion) region;
            Vector v = getMirroredValues(rg.getOriginX(), rg.getOriginY(), rg.getOriginZ());
            return (T) new SphereRegion(null, v, rg.getRadius());

        } else if (region instanceof EmptyRegion) {

            return (T) new EmptyRegion("");

        } else if (region instanceof ComplementRegion) {

            ComplementRegion rg = (ComplementRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(mirrorRegion(rg1));
            }
            return (T) new ComplementRegion(null, regions);

        } else if (region instanceof IntersectRegion) {

            IntersectRegion rg = (IntersectRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(mirrorRegion(rg1));
            }
            return (T) new IntersectRegion(null, regions);

        } else if (region instanceof NegativeRegion) {

            NegativeRegion rg = (NegativeRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(mirrorRegion(rg1));
            }
            return (T) new NegativeRegion(null, regions);

        } else if (region instanceof UnionRegion) {

            UnionRegion rg = (UnionRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(mirrorRegion(rg1));
            }
            return (T) new UnionRegion(null, regions);

        } else if (region instanceof TranslatedRegion) {

            TranslatedRegion rg = (TranslatedRegion) region;
            return (T) new TranslatedRegion(null, mirrorRegion(rg.getRegion()));

        } else if (region instanceof MirroredRegion) {

            MirroredRegion rg = (MirroredRegion) region;
            return (T) new MirroredRegion(null, mirrorRegion(rg.getRegion()));

        }
        return null;

    }

    public Vector getMirroredValues(double x1, double y1, double z1) {
        Vector vector = new Vector(x1, y1, z1).minus(origin);
        vector = vector.minus(normal.times(vector.dot(normal)).times(2)).add(origin);
        vector = new Vector(round(vector.getX()), round(vector.getY()), round(vector.getZ()));
        return vector;
    }

    public double round(double d) {
        return (double) Math.round(d * 10) / 10D;
    }

}
