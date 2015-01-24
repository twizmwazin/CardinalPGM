package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class BlockParser extends RegionParser{

    private final Vector vector;

    public BlockParser(Element element) {
        super(element.getAttributeValue("name"));
        double x, y, z;
        try {
            String[] coords = element.getAttributeValue("location").trim().split(",");
            x = NumUtils.parseDouble(coords[0]);
            y = NumUtils.parseDouble(coords[1]);
            z = NumUtils.parseDouble(coords[2]);
        } catch (NullPointerException ex) {
            String[] coords = element.getText().trim().split(",");
            x = NumUtils.parseDouble(coords[0]);
            y = NumUtils.parseDouble(coords[1]);
            z = NumUtils.parseDouble(coords[2]);
        }
        this.vector = new BlockVector(x, y, z);
    }

    public Vector getVector() {
        return vector;
    }
}
