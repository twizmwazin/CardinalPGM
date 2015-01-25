package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class CircleParser extends RegionParser {

    private final double centerX, centerZ, radius;

    public CircleParser(Element element) {
        super(element.getAttributeValue("name"));
        String[] centArray = element.getAttributeValue("center").contains(",") ? 
                element.getAttributeValue("center").trim().replaceAll(" ", "").split(",") :
                element.getAttributeValue("center").trim().replaceAll(" ", ",").split(",");
        this.centerX = NumUtils.parseDouble(centArray[0].trim());
        this.centerZ = NumUtils.parseDouble(centArray[1].trim());
        this.radius = NumUtils.parseDouble(element.getAttributeValue("radius"));
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
