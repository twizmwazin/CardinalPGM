package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.FlooredVector;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class BlockParser extends RegionParser{

    private final Vector vector;

    public BlockParser(Element element) {
        super(element.getAttributeValue("name"));
        double x, y, z;
        String working = element.getAttributeValue("location") == null ? element.getText() : element.getAttributeValue("location");
        if (element.getText().contains(",")) {
            x = NumUtils.parseDouble(working.split(",")[0].trim());
            y = NumUtils.parseDouble(working.split(",")[1].trim());
            z = NumUtils.parseDouble(working.split(",")[2].trim());
        } else {
            x = NumUtils.parseDouble(working.trim().replaceAll(" ", ",").split(",")[0]);
            y = NumUtils.parseDouble(working.trim().replaceAll(" ", ",").split(",")[1]);
            z = NumUtils.parseDouble(working.trim().replaceAll(" ", ",").split(",")[2]);
        }
        this.vector = new FlooredVector(x, y, z);
    }

    public Vector getVector() {
        return vector;
    }
}
