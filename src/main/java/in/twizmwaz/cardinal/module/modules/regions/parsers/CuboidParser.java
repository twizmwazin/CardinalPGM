package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class CuboidParser extends RegionParser {

    private final Vector min, max;
    
    public CuboidParser(Element element) {
        super(element.getAttributeValue("name"));
        String[] mins = element.getAttributeValue("min").replaceAll(" ", "").split(",");
        String[] maxs = element.getAttributeValue("max").replaceAll(" ", "").split(",");
        this.min = new Vector(Double.parseDouble(mins[0]), Double.parseDouble(mins[1]), Double.parseDouble(mins[2]));
        this.max = new Vector(Double.parseDouble(maxs[0]), Double.parseDouble(maxs[1]), Double.parseDouble(maxs[2]));
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }
}
