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
        String[] mins = element.getAttribute("min").getValue().split(",");
        this.xMin = NumUtils.parseDouble(mins[0]);
        this.yMin = NumUtils.parseDouble(mins[1]);
        this.zMin = NumUtils.parseDouble(mins[2]);
        String[] maxs = element.getAttribute("max").getValue().split(",");
        this.xMax = NumUtils.parseDouble(maxs[0]);
        this.yMax = NumUtils.parseDouble(maxs[1]);
        this.zMax = NumUtils.parseDouble(maxs[2]);
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
