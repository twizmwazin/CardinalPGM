package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.jdom2.Element;

public class RectangleParser extends RegionParser {

    private final double xMin, zMin, xMax, zMax;

    public RectangleParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
        String min = element.getAttributeValue("min");
        String max = element.getAttributeValue("max");
        String[] mins = min.contains(",") ? min.split(",") : min.trim().replaceAll(" ", ",").split(",");
        String[] maxs = max.contains(",") ? max.split(",") : max.trim().replaceAll(" ", ",").split(",");
        xMin = Numbers.parseDouble(mins[0].trim());
        zMin = Numbers.parseDouble(mins[1].trim());
        xMax = Numbers.parseDouble(maxs[0].trim());
        zMax = Numbers.parseDouble(maxs[1].trim());
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
