package in.twizmwaz.cardinal.module.modules.regions.type.modifications;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers.TranslateParser;
import in.twizmwaz.cardinal.module.modules.regions.type.*;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;

public class TranslatedRegion extends RegionModule {

    private final Vector offset;
    private final RegionModule region;

    public TranslatedRegion(String name, RegionModule base, Vector offset) {
        super(name);
        this.offset = offset;
        this.region = translateRegion(base);
    }

    public TranslatedRegion(String name, RegionModule region) {
        super(name);
        this.offset = null;
        this.region = region;
    }

    public TranslatedRegion(TranslateParser parser) {
        this(parser.getName(), parser.getBase(), parser.getOffset());
    }

    public double getXOffset() {
        return offset.getX();
    }

    public double getYOffset() {
        return offset.getY();
    }

    public double getZOffset() {
        return offset.getZ();
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
    private <T extends RegionModule> T translateRegion(T region) {

        double x = getXOffset();
        double y = getYOffset();
        double z = getZOffset();
        if (region instanceof PointRegion) {

            PointRegion rg = (PointRegion) region;
            return (T) new PointRegion(null, rg.getX() + x, rg.getY() + y, rg.getZ() + z);

        } else if (region instanceof BlockRegion) {

            BlockRegion rg = (BlockRegion) region;
            return (T) new BlockRegion(null, rg.getX() + x, rg.getY() + y, rg.getZ() + z);

        } else if (region instanceof CircleRegion) {

            CircleRegion rg = (CircleRegion) region;
            return (T) new CircleRegion(null, rg.getBaseX() + x, rg.getBaseZ() + z, rg.getRadius());

        } else if (region instanceof CylinderRegion) {

            CylinderRegion rg = (CylinderRegion) region;
            return (T) new CylinderRegion(null, new Vector(rg.getBaseX() + x, rg.getBaseY() + y, rg.getBaseZ() + z), rg.getRadius(), rg.getHeight());

        } else if (region instanceof RectangleRegion) {

            RectangleRegion rg = (RectangleRegion) region;
            return (T) new RectangleRegion(null, rg.getXMin() + x, rg.getZMin() + z, rg.getXMax() + x, rg.getZMax() + z);

        } else if (region instanceof CuboidRegion) {

            CuboidRegion rg = (CuboidRegion) region;
            return (T) new CuboidRegion(null, rg.getXMin() + x, rg.getYMin() + y, rg.getZMin() + z, rg.getXMax() + x, rg.getYMax() + y, rg.getZMax() + z);

        } else if (region instanceof SphereRegion) {

            SphereRegion rg = (SphereRegion) region;
            return (T) new SphereRegion(null, new Vector(rg.getOriginX() + x, rg.getOriginY() + y, rg.getOriginZ() + z), rg.getRadius());

        } else if (region instanceof EmptyRegion) {

            return (T) new EmptyRegion("");

        } else if (region instanceof ComplementRegion) {

            ComplementRegion rg = (ComplementRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(translateRegion(rg1));
            }
            return (T) new ComplementRegion(null, regions);

        } else if (region instanceof IntersectRegion) {

            IntersectRegion rg = (IntersectRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(translateRegion(rg1));
            }
            return (T) new IntersectRegion(null, regions);

        } else if (region instanceof NegativeRegion) {

            NegativeRegion rg = (NegativeRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(translateRegion(rg1));
            }
            return (T) new NegativeRegion(null, regions);

        } else if (region instanceof UnionRegion) {

            UnionRegion rg = (UnionRegion) region;
            ModuleCollection<RegionModule> regions = new ModuleCollection<>();
            for (RegionModule rg1 : rg.getRegions()) {
                regions.add(translateRegion(rg1));
            }
            return (T) new UnionRegion(null, regions);

        } else if (region instanceof TranslatedRegion) {

            TranslatedRegion rg = (TranslatedRegion) region;
            return (T) new TranslatedRegion(null, translateRegion(rg.getRegion()));

        } else if (region instanceof MirroredRegion) {

            MirroredRegion rg = (MirroredRegion) region;
            return (T) new MirroredRegion(null, translateRegion(rg.getRegion()));

        }
        return null;

    }

}
