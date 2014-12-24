package in.twizmwaz.cardinal.regions.parsers.modifiers;

import in.twizmwaz.cardinal.regions.Region;
import org.jdom2.Element;

public class TranslateParser {

    private final Region base;
    private final double xOffset, yOffset, zOffset;

    public TranslateParser(Element element) {
        this.base = Region.getRegion(element.getChildren().get(0));
        String[] offset = element.getAttributeValue("offset").split(",");
        this.xOffset = Double.parseDouble(offset[0]);
        this.yOffset = Double.parseDouble(offset[1]);
        this.zOffset = Double.parseDouble(offset[2]);
    }

    public Region getBase() {
        return base;
    }

    public double getXOffset() {
        return xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public double getZOffset() {
        return zOffset;
    }
}
