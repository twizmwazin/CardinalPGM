package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class SphereParser extends RegionParser {

    private final Vector origin;
    private double radius;

    public SphereParser(Element element) {
        super(element.getAttributeValue("name"));
        String origin = element.getAttributeValue("origin");
        String[] origina = origin.contains(",") ? origin.split(",") : origin.trim().replaceAll(" ", ",").split(",");
        this.origin = new Vector(Numbers.parseDouble(origina[0].trim()), Numbers.parseDouble(origina[1].trim()), Numbers.parseDouble(origina[2].trim()));
        this.radius = Numbers.parseDouble(element.getAttributeValue("radius").trim());
    }

    public Vector getOrigin() {
        return origin;
    }

    public double getRadius() {
        return radius;
    }

}
