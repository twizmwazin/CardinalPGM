package in.twizmwaz.cardinal.module.modules.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class RectangleParser {

    private String name;
    private double xMin;
    private double zMin;
    private double xMax;
    private double zMax;

    public RectangleParser(Node node) {
        Double[] results = null;
        name = node.getAttributes().getNamedItem("name").getNodeValue();
        String min = node.getAttributes().getNamedItem("min").getNodeValue();
        String max = node.getAttributes().getNamedItem("max").getNodeValue();
        String[] mins = min.split(",");
        String[] maxs = max.split(",");
        xMin = Double.parseDouble(mins[0]);
        zMin = Double.parseDouble(mins[1]);
        xMax = Double.parseDouble(maxs[0]);
        zMax = Double.parseDouble(maxs[1]);
        
    }

    public String getName() {
        return name;
    }

    public double getXMin() {
        return xMin;
    }

    public double getZMin() {
        return zMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getZMax() {
        return zMax;
    }

}
