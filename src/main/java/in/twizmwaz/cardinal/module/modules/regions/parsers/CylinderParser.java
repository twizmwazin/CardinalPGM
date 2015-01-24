package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class CylinderParser extends RegionParser {

    private final Vector base;
    private final double radius, height;

    public CylinderParser(Element element) {
        super(element.getAttributeValue("name"));
        this.radius = NumUtils.parseDouble(element.getAttribute("radius").getValue().replaceAll(" ", ""));
        this.height = NumUtils.parseDouble(element.getAttribute("height").getValue().replaceAll(" ", ""));
        String[] split = element.getAttribute("base").getValue().replaceAll(" ", "").split(",");
        this.base = new Vector(NumUtils.parseDouble(split[0]), NumUtils.parseDouble(split[1]), NumUtils.parseDouble(split[2]));
    }

    public Vector getBase() {
        return base;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

}
