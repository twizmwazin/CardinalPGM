package in.twizmwaz.cardinal.regions.parsers;

import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

/**
 * Created by kevin on 10/26/14.
 */
public class CuboidParser {

    private double xMin;
    private double yMin;
    private double zMin;
    private double xMax;
    private double yMax;
    private double zMax;

    public CuboidParser(Element element) {
        String[] mins = element.getAttribute("min").getValue().replaceAll(" ", "").split(",");
        String[] maxs = element.getAttribute("max").getValue().replaceAll(" ", "").split(",");
        double xMin = NumUtils.parseDouble(mins[0]);
        double yMin = NumUtils.parseDouble(mins[1]);
        double zMin = NumUtils.parseDouble(mins[2]);
        double xMax = NumUtils.parseDouble(maxs[0]);
        double yMax = NumUtils.parseDouble(maxs[1]);
        double zMax = NumUtils.parseDouble(maxs[2]);
        this.xMin = xMin < xMax ? xMin : xMax;
        this.yMin = yMin < yMax ? yMin : yMax;
        this.zMin = zMin < zMax ? zMin : zMax;
        this.xMax = xMin > xMax ? xMin : xMax;
        this.yMax = yMin > yMax ? yMin : yMax;
        this.zMax = zMin > zMax ? zMin : zMax;
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
