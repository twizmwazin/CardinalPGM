package in.twizmwaz.cardinal.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class CircleParser {

    private String name;
    private double centerX;
    private double centerZ;
    private double radius;

    public CircleParser(Node node) {
        if (node.getAttributes().getNamedItem("name") != null){
            this.name = node.getAttributes().getNamedItem("name").getNodeValue();
        }
        String[] centArray = node.getAttributes().getNamedItem("center").getNodeValue().split(",");
        this.centerX = Double.parseDouble(centArray[0]);
        this.centerZ = Double.parseDouble(centArray[1]);
        this.radius = Double.parseDouble(node.getAttributes().getNamedItem("radius").getNodeValue());

    }

    public String getName() {
        return name;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterZ() {
        return centerZ;
    }

    public double getRadius() {
        return radius;
    }
}
