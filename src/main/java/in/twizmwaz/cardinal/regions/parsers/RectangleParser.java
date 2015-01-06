package in.twizmwaz.cardinal.regions.parsers;

import in.twizmwaz.cardinal.util.NumUtils;
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
        String[] mins = min.replaceAll(" ", "").split(",");
        String[] maxs = max.replaceAll(" ", "").split(",");
        xMin = NumUtils.parseDouble(mins[0]);
        zMin = NumUtils.parseDouble(mins[1]);
        xMax = NumUtils.parseDouble(maxs[0]);
        zMax = NumUtils.parseDouble(maxs[1]);
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
