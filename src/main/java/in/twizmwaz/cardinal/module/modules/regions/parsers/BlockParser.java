package in.twizmwaz.cardinal.module.modules.regions.parsers;

import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.jdom2.Element;

public class BlockParser extends RegionParser{

    private double x, y, z;

    public BlockParser(Element element) {
        super(element.getAttributeValue("name"));
        try {
            String[] coords = element.getAttributeValue("location").replaceAll(" ", "").split(",");
            this.x = NumUtils.parseDouble(coords[0]);
            this.y = NumUtils.parseDouble(coords[1]);
            this.z = NumUtils.parseDouble(coords[2]);
        } catch (NullPointerException ex) {
            String[] coords = element.getText().replaceAll(" ", "").split(",");
            this.x = NumUtils.parseDouble(coords[0]);
            this.y = NumUtils.parseDouble(coords[1]);
            this.z = NumUtils.parseDouble(coords[2]);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
