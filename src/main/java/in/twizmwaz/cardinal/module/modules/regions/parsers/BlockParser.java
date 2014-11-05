package in.twizmwaz.cardinal.module.modules.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class BlockParser {

    private String name;
    private double x;
    private double y;
    private double z;

    public BlockParser(Node node) {
        if (node.getAttributes().getNamedItem("locaton") != null) {
            if (node.getAttributes().getNamedItem("name").getNodeValue() != null ) {
                this.name = node.getAttributes().getNamedItem("name").getNodeValue();
            }
            String[] coords = node.getAttributes().getNamedItem("location").toString().split(",");
            this.x = Double.parseDouble(coords[0]);
            this.y = Double.parseDouble(coords[1]);
            this.z = Double.parseDouble(coords[2]);

        } else {

            if (node.getAttributes().getNamedItem("name").getNodeValue() != null ) {
                this.name = node.getAttributes().getNamedItem("name").getNodeValue();
            }
            String[] coords = node.getNodeValue().split(",");
            this.x = Double.parseDouble(coords[0]);
            this.y = Double.parseDouble(coords[1]);
            this.z = Double.parseDouble(coords[2]);
        }
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
