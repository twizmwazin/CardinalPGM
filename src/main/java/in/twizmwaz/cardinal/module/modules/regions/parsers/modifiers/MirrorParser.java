package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.jdom2.Element;

public class MirrorParser extends RegionParser {

    private RegionModule base;
    private final double xOrigin, yOrigin, zOrigin;

    public MirrorParser(Element element) {
        super(element.getAttributeValue("name"));
        try {
            this.base = RegionModuleBuilder.getRegion(element.getChildren().get(0));
        } catch (IndexOutOfBoundsException e) {
            for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                if (regionModule.getName().equalsIgnoreCase(element.getAttributeValue("region"))) this.base = regionModule;
            }
        }
        String[] origin = element.getAttributeValue("normal").replaceAll(" ", "").split(",");
        this.xOrigin = Double.parseDouble(origin[0]);
        this.yOrigin = Double.parseDouble(origin[1]);
        this.zOrigin = Double.parseDouble(origin[2]);
    }
    
    public RegionModule getBase() {
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
