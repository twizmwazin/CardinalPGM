package in.twizmwaz.cardinal.regions;

import in.twizmwaz.cardinal.regions.parsers.*;
import in.twizmwaz.cardinal.regions.parsers.modifiers.CombinationParser;
import in.twizmwaz.cardinal.regions.point.PointParser;
import in.twizmwaz.cardinal.regions.type.*;
import in.twizmwaz.cardinal.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.regions.point.PointRegion;
import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public abstract class Region {

    public Region() {
    }

    public abstract boolean contains(BlockRegion region);

    public abstract boolean contains(PointRegion point);

    public abstract PointRegion getRandomPoint();

    public static Region newRegion(Element element) {
        switch (element.getName()) {
            case "block":
                return new BlockRegion(new BlockParser(element));
            case "point":
                return new PointRegion(new PointParser(element));
            case "circle":
                return new CircleRegion(new CircleParser(element));
            case "cuboid":
                return new CuboidRegion(new CuboidParser(element));
            case "cylinder":
                return new CylinderRegion(new CylinderParser(element));
            case "empty":
                return new EmptyRegion(new EmptyParser(element));
            case "rectangle":
                return new RectangleRegion(new RectangleParser(element));
            case "sphere":
                return new SphereRegion(new SphereParser(element));
            case "complement":
                return new ComplementRegion(new CombinationParser(element));
            case "intersect":
                return new IntersectRegion(new CombinationParser(element));
            case "negative":
                return new NegativeRegion(new CombinationParser(element));
            case "union":
                return new UnionRegion((new CombinationParser(element)));
            default:
                return  null;
        }
    }


}
