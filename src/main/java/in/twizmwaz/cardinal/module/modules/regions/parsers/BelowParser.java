package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class BelowParser extends RegionParser {

    private final Vector vector;

    public BelowParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
        Double
                x = Numbers.parseDouble(element.getAttributeValue("x"), Double.POSITIVE_INFINITY),
                y = Numbers.parseDouble(element.getAttributeValue("y"), Double.POSITIVE_INFINITY),
                z = Numbers.parseDouble(element.getAttributeValue("z"), Double.POSITIVE_INFINITY);
        vector = new Vector(x, y, z);
    }

    public Vector getVector() {
        return vector;
    }

}