package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class BlockParser extends RegionParser {

    private final Vector vector;

    public BlockParser(Element element) {
        super(element.getAttributeValue("name") != null ? element.getAttributeValue("name") : element.getAttributeValue("id"));
        double x, y, z;
        String working = element.getAttributeValue("location") == null ? element.getText() : element.getAttributeValue("location");
        if (element.getText().contains(",")) {
            x = Numbers.parseDouble(working.split(",")[0].trim());
            y = Numbers.parseDouble(working.split(",")[1].trim());
            z = Numbers.parseDouble(working.split(",")[2].trim());
        } else {
            x = Numbers.parseDouble(working.trim().replaceAll(" ", ",").split(",")[0]);
            y = Numbers.parseDouble(working.trim().replaceAll(" ", ",").split(",")[1]);
            z = Numbers.parseDouble(working.trim().replaceAll(" ", ",").split(",")[2]);
        }
        this.vector = new Vector(Math.floor(x), Math.floor(y), Math.floor(z));
    }

    public Vector getVector() {
        return vector;
    }
}
