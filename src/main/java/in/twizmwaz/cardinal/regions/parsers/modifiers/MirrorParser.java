package in.twizmwaz.cardinal.regions.parsers.modifiers;

import in.twizmwaz.cardinal.regions.Region;
import org.jdom2.Element;

public class MirrorParser {

    private final Region base;
    private final double xOrigin, yOrigin, zOrigin;

    public MirrorParser(Element element) {
        this.base = Region.getRegion(element.getChildren().get(0));
        String[] origin = element.getAttributeValue("normal").replaceAll(" ", "").split(",");
        this.xOrigin = Double.parseDouble(origin[0]);
        this.yOrigin = Double.parseDouble(origin[1]);
        this.zOrigin = Double.parseDouble(origin[2]);
    }

    public Region getBase() {
        return base;
    }

    public double getXOrigin() {
        return xOrigin;
    }

    public double getYOrigin() {
        return yOrigin;
    }

    public double getZOrigin() {
        return zOrigin;
    }

}
