package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.bukkit.util.Vector;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CuboidParser extends RegionParser {

    private final Vector min, max;
    
    public CuboidParser(Element element) {
        super(element.getAttributeValue("name"));
        List<String> values = new ArrayList<>();
        values.addAll(Arrays.asList(element.getAttributeValue("min").contains(",") ? 
                element.getAttributeValue("min").trim().split(",") : element.getAttributeValue("min").trim().split(" ")));
        values.addAll(Arrays.asList(element.getAttributeValue("max").trim().replaceAll(" ", ",").split(",")));
        for (String string : values) {
            values.set(values.indexOf(string), string.trim());
            if (string.equalsIgnoreCase("oo")) values.set(values.indexOf(string), "256");
            if (string.equalsIgnoreCase("-oo")) values.set(values.indexOf(string), "0");
        }
        this.min = new Vector(Double.parseDouble(values.get(0)), Double.parseDouble(values.get(1)), Double.parseDouble(values.get(2)));
        this.max = new Vector(Double.parseDouble(values.get(3)), Double.parseDouble(values.get(4)), Double.parseDouble(values.get(5)));
    }

    public Vector getMin() {
        return min;
    }

    public Vector getMax() {
        return max;
    }
}
