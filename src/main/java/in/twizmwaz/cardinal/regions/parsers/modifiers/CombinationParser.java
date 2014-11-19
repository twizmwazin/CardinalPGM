package in.twizmwaz.cardinal.regions.parsers.modifiers;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.*;
import in.twizmwaz.cardinal.regions.type.*;
import in.twizmwaz.cardinal.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import org.jdom2.Element;

import java.util.List;

/**
 * Created by kevin on 10/27/14.
 */
public class CombinationParser {

    private List<Region> regions;

    public CombinationParser(Element element) {
        List<Element> children = element.getChildren();
        for (Element child : children) {
            if (element.getName().equalsIgnoreCase("block")) {
                regions.add(new BlockRegion(new BlockParser(child)));
            }
            if (element.getName().equalsIgnoreCase("circle")) {
                regions.add(new CircleRegion(new CircleParser(child)));
            }
            if (element.getName().equalsIgnoreCase("cuboid")) {
                regions.add(new CuboidRegion(new CuboidParser(child)));
            }
            if (element.getName().equalsIgnoreCase("cylinder")) {
                regions.add(new CylinderRegion(new CylinderParser(child)));
            }
            if (element.getName().equalsIgnoreCase("empty")) {
                regions.add(new EmptyRegion(new EmptyParser(child)));
            }
            if (element.getName().equalsIgnoreCase("rectangle")) {
                regions.add(new RectangleRegion(new RectangleParser(child)));
            }
            if (element.getName().equalsIgnoreCase("sphere")) {
                regions.add(new SphereRegion(new SphereParser(child)));
            }
            if (element.getName().equalsIgnoreCase("complement")) {
                regions.add(new ComplementRegion(new CombinationParser(child)));
            }
            if (element.getName().equalsIgnoreCase("intersect")) {
                regions.add(new IntersectRegion(new CombinationParser(child)));
            }
            if (element.getName().equalsIgnoreCase("negative")) {
                regions.add(new NegativeRegion(new CombinationParser(child)));
            }
            if (element.getName().equalsIgnoreCase("union")) {
                regions.add(new UnionRegion(new CombinationParser(child)));
            }

        }

    }

    public String getName() {
        return null;
    }

    public List<Region> getRegions() {
        return regions;
    }

}
