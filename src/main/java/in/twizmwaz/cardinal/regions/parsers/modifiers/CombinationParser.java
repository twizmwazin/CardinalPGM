package in.twizmwaz.cardinal.regions.parsers.modifiers;

import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.parsers.*;
import in.twizmwaz.cardinal.regions.type.*;
import in.twizmwaz.cardinal.regions.type.combinations.ComplementRegion;
import in.twizmwaz.cardinal.regions.type.combinations.IntersectRegion;
import in.twizmwaz.cardinal.regions.type.combinations.NegativeRegion;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.util.XMLHandler;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by kevin on 10/27/14.
 */
public class CombinationParser {

    private List<Region> regions;

    public CombinationParser(Node node) {
        List<Node> children = XMLHandler.nodeListToList(node.getChildNodes());
        for (Node child : children) {
            if (node.getNodeName().equalsIgnoreCase("block")) {
                regions.add(new BlockRegion(new BlockParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("circle")) {
                regions.add(new CircleRegion(new CircleParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("cuboid")) {
                regions.add(new CuboidRegion(new CuboidParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("cylinder")) {
                regions.add(new CylinderRegion(new CylinderParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("empty")) {
                regions.add(new EmptyRegion(new EmptyParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("rectangle")) {
                regions.add(new RectangleRegion(new RectangleParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("sphere")) {
                regions.add(new SphereRegion(new SphereParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("complement")) {
                regions.add(new ComplementRegion(new CombinationParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("intersect")) {
                regions.add(new IntersectRegion(new CombinationParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("negative")) {
                regions.add(new NegativeRegion(new CombinationParser(child)));
            }
            if (node.getNodeName().equalsIgnoreCase("union")) {
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
