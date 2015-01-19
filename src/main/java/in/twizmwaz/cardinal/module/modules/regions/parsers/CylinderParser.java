package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class CylinderParser extends RegionParser {

    private final double baseX, baseY, baseZ, radius, height;

    public CylinderParser(Element element) {
        super(element.getAttributeValue("name"));
        this.radius = NumUtils.parseDouble(element.getAttribute("radius").getValue().replaceAll(" ", ""));
        this.height = NumUtils.parseDouble(element.getAttribute("height").getValue().replaceAll(" ", ""));
        String[] split = element.getAttribute("base").getValue().replaceAll(" ", "").split(",");
        this.baseX = NumUtils.parseDouble(split[0]);
        this.baseY = NumUtils.parseDouble(split[1]);
        this.baseZ = NumUtils.parseDouble(split[2]);
    }

    public double getBaseX() {
        return baseX;
    }

    public double getBaseY() {
        return baseY;
    }

    public double getBaseZ() {
        return baseZ;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

}
