package in.twizmwaz.cardinal.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class CylinderParser {

    private String name;
    private double baseX;
    private double baseY;
    private double baseZ;
    private double radius;
    private double height;

    public CylinderParser(Node node) {
        this.name = node.getAttributes().getNamedItem("name").getNodeValue();
        this.radius = Double.parseDouble(node.getAttributes().getNamedItem("radius").getNodeValue());
        this.height = Double.parseDouble(node.getAttributes().getNamedItem("height").getNodeValue());

        String[] split = node.getAttributes().getNamedItem("base").getNodeValue().split(",");
        this.baseX = Double.parseDouble(split[0]);
        this.baseY = Double.parseDouble(split[1]);
        this.baseZ = Double.parseDouble(split[2]);
    }

    public String getName() {
        return name;
    }

    public double getBaseX() {
        return baseX;
    }

    public double getBaseY() {
        return baseY;
    }

    public double getBaseZ() {
        return baseZ;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

}
