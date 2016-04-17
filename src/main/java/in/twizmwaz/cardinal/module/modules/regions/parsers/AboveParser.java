package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class AboveParser extends RegionParser {

    private final Vector vector;

    public AboveParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
        Double
                x = Numbers.parseDouble(element.getAttributeValue("x"), Double.MIN_VALUE),
                y = Numbers.parseDouble(element.getAttributeValue("y"), Double.MIN_VALUE),
                z = Numbers.parseDouble(element.getAttributeValue("z"), Double.MIN_VALUE);
        vector = new Vector(x, y, z);
    }

    public Vector getVector() {
        return vector;
    }

}
