package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class SphereParser extends RegionParser {
    
    private final double originX, originY, originZ;
    private double radius;

    public SphereParser(Element element) {
        super(element.getAttributeValue("name"));
        String origin = element.getAttributeValue("origin");
        String[] origina = origin.replaceAll(" ", "").split(",");
        this.originX = NumUtils.parseDouble(origina[0]);
        this.originY = NumUtils.parseDouble(origina[1]);
        this.originZ = NumUtils.parseDouble(origina[2]);
        this.radius = NumUtils.parseDouble(element.getAttributeValue("radius").replaceAll(" ", ""));
    }

    public double getOriginX() {
        return originX;
    }

    public double getOriginY() {
        return originY;
    }

    public double getOriginZ() {
        return originZ;
    }

    public double getRadius() {
        return radius;
    }

}
