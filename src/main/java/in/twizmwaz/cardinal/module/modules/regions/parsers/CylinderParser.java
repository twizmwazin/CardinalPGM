package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class CylinderParser extends RegionParser {

    private final Vector base;
    private final double radius, height;

    public CylinderParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
        this.radius = Numbers.parseDouble(element.getAttributeValue("radius").trim());
        this.height = Numbers.parseDouble(element.getAttributeValue("height").trim());
        String[] split = element.getAttributeValue("base").contains(",") ? element.getAttributeValue("base").split(",") : element.getAttributeValue("base").replaceAll(" ", ",").split(",");
        this.base = new Vector(Numbers.parseDouble(split[0].trim()), Numbers.parseDouble(split[1].trim()), Numbers.parseDouble(split[2].trim()));
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
