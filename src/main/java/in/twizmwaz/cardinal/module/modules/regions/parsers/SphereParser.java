package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.FlooredVector;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class SphereParser extends RegionParser {
    
    private final Vector origin;
    private double radius;

    public SphereParser(Element element) {
        super(element.getAttributeValue("name"));
        String origin = element.getAttributeValue("origin");
        String[] origina = origin.contains(",") ? origin.split(",") : origin.trim().replaceAll(" ", ",").split(",");
        this.origin = new FlooredVector(NumUtils.parseDouble(origina[0].trim()), NumUtils.parseDouble(origina[1].trim()), NumUtils.parseDouble(origina[2].trim()));
        this.radius = NumUtils.parseDouble(element.getAttributeValue("radius").trim());
    }

    public Vector getOrigin() {
        return origin;
    }

    public double getRadius() {
        return radius;
    }

}
