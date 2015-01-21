package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.jdom2.Element;

public class TranslateParser extends RegionParser{

    private final RegionModule base;
    private final double xOffset, yOffset, zOffset;

    public TranslateParser(Element element) {
        super(element.getAttributeValue("name"));
        this.base = element.getChildren().size() > 0 ? 
                RegionModuleBuilder.getRegion(element.getChildren().get(0)) : 
                RegionModuleBuilder.getRegion(element.getAttributeValue("region"));
        String[] offset = element.getAttributeValue("offset").replaceAll(" ", "").split(",");
        this.xOffset = Double.parseDouble(offset[0]);
        this.yOffset = Double.parseDouble(offset[1]);
        this.zOffset = Double.parseDouble(offset[2]);
    }

    public RegionModule getBase() {
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
