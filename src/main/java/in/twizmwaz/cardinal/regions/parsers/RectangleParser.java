package in.twizmwaz.cardinal.regions.parsers;

import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public class RectangleParser {

    private double xMin;
    private double zMin;
    private double xMax;
    private double zMax;

    public RectangleParser(Element element) {
        String min = element.getAttribute("min").getValue();
        String max = element.getAttribute("max").getValue();
        String[] mins = min.split(",");
        String[] maxs = max.split(",");
        xMin = Double.parseDouble(mins[0]);
        zMin = Double.parseDouble(mins[1]);
        xMax = Double.parseDouble(maxs[0]);
        zMax = Double.parseDouble(maxs[1]);
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
