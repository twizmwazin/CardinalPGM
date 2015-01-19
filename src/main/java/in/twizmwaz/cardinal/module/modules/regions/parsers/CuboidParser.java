package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class CuboidParser extends RegionParser {

    private final double xMin, yMin, zMin, xMax, yMax, zMax;
    
    public CuboidParser(Element element) {
        super(element.getAttributeValue("name"));
        String[] mins = element.getAttributeValue("min").replaceAll(" ", "").split(",");
        String[] maxs = element.getAttributeValue("max").replaceAll(" ", "").split(",");
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
