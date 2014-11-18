package in.twizmwaz.cardinal.regions.parsers;

import org.w3c.dom.Node;

/**
 * Created by kevin on 10/26/14.
 */
public class CuboidParser {

    private String name;
    private double xMin;
    private double yMin;
    private double zMin;
    private double xMax;
    private double yMax;
    private double zMax;

    public CuboidParser(Node node) {
        if (node.getAttributes().getNamedItem("name") != null){
            this.name = node.getAttributes().getNamedItem("name").getNodeValue();
        }

        String[] mins = node.getAttributes().getNamedItem("min").getNodeValue().split(",");
        this.xMin = Double.parseDouble(mins[0]);
        this.yMin = Double.parseDouble(mins[1]);
        this.zMin = Double.parseDouble(mins[2]);

        String[] maxs = node.getAttributes().getNamedItem("max").getNodeValue().split(",");
        this.xMax = Double.parseDouble(maxs[0]);
        this.yMax = Double.parseDouble(maxs[1]);
        this.zMax = Double.parseDouble(maxs[2]);

    }

    public String getName() {
        return name;
    }

    public double getXMin() {
        return xMin;
    }

    public double getYMin() {
        return yMin;
    }

    public double getZMin() {
        return zMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMax() {
        return yMax;
    }

    public double getZMax() {
        return zMax;
    }

}
