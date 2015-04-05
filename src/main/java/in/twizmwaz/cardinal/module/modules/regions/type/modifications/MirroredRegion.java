package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.MirrorParser;
import in.twizmwaz.cardinal.module.modules.regions.type.*;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class MirroredRegion extends RegionModule {

    private final RegionModule region;
    private final Vector origin, normal;

    public MirroredRegion(String name, RegionModule base, Vector origin, Vector normal) {
        super(name);
        this.origin = origin;
        this.normal = normal;
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

    @SuppressWarnings({"unchecked"})
    private <T extends RegionModule> T mirrorRegion(T region) {


        if (region instanceof PointRegion) {

            PointRegion rg = (PointRegion) region;
            int[] v1 = getMirroredValues(rg.getX(), rg.getY(), rg.getZ());
            return (T) new PointRegion(null, rg.getX() + v1[0], rg.getY() + v1[1], rg.getZ() + v1[2]);

        } else if (region instanceof BlockRegion) {

            BlockRegion rg = (BlockRegion) region;
            int[] v1 = getMirroredValues(rg.getX(), rg.getY(), rg.getZ());
            return (T) new BlockRegion(null, rg.getX() + v1[0], rg.getY() + v1[1], rg.getZ() + v1[2]);

        } else if (region instanceof CircleRegion) {

            CircleRegion rg = (CircleRegion) region;
            int[] v1 = getMirroredValues(rg.getBaseX(), rg.getBaseY(), rg.getBaseZ());
            return (T) new CircleRegion(null, rg.getBaseX() + v1[0], rg.getBaseZ() + v1[2], rg.getRadius());

        } else if (region instanceof CylinderRegion) {

            CylinderRegion rg = (CylinderRegion) region;
            int[] v1 = getMirroredValues(rg.getBaseX(), rg.getBaseY(), rg.getBaseZ());
            return (T) new CylinderRegion(null, new Vector(rg.getBaseX() + v1[0], rg.getBaseY() + v1[1], rg.getBaseZ() + v1[2]), rg.getRadius(), rg.getHeight());

        } else if (region instanceof RectangleRegion) {

            RectangleRegion rg = (RectangleRegion) region;
            int[] v1 = getMirroredValues(rg.getXMin(), rg.getYMin(), rg.getZMin());
            int[] v2 = getMirroredValues(rg.getXMax(), rg.getYMax(), rg.getZMax());
            return (T) new RectangleRegion(null, rg.getXMin() + v1[0], rg.getZMin() + v1[2], rg.getXMax() + v2[0], rg.getZMax() + v2[2]);

        } else if (region instanceof CuboidRegion) {

            CuboidRegion rg = (CuboidRegion) region;
            int[] v1 = getMirroredValues(rg.getXMin(), rg.getYMin(), rg.getZMin());
            int[] v2 = getMirroredValues(rg.getXMax(), rg.getYMax(), rg.getZMax());
            return (T) new CuboidRegion(null, rg.getXMin() + v1[0], rg.getYMin() + v1[1], rg.getZMin() + v1[2], rg.getXMax() + v2[0], rg.getYMax() + v2[1], rg.getZMax() + v2[2]);

        } else if (region instanceof SphereRegion) {

            SphereRegion rg = (SphereRegion) region;
            int[] v1 = getMirroredValues(rg.getOriginX(), rg.getOriginY(), rg.getOriginZ());
            return (T) new SphereRegion(null, new Vector(rg.getOriginX() + v1[0], rg.getOriginY() + v1[1], rg.getOriginZ() + v1[2]), rg.getRadius());

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

    public int[] getMirroredValues(double x1, double y1, double z1) {
        int x = 0;
        int y = 0;
        int z = 0;
        if (normal.getX() != 0) {
            x = (int) Math.floor((x1 - origin.getX()) * -2.0);
        }
        if (normal.getY() != 0) {
            y = (int) Math.floor((y1 - origin.getY()) * -2.0);
        }
        if (normal.getZ() != 0) {
            z = (int) Math.floor((z1 - origin.getZ()) * -2.0);
        }
        int[] values = {x, y, z};
        return values;
    }
}
