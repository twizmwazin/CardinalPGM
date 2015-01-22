package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class MirrorParser extends RegionParser {

    private RegionModule base;
    private final Vector normal;

    public MirrorParser(Element element) {
        super(element.getAttributeValue("name"));
        try {
            this.base = RegionModuleBuilder.getRegion(element.getChildren().get(0));
        } catch (IndexOutOfBoundsException e) {
            for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                if (regionModule.getName().equalsIgnoreCase(element.getAttributeValue("region"))) this.base = regionModule;
            }
        }
        String[] origin = element.getAttributeValue("origin").replaceAll(" ", "").split(",");
        String[] normal = element.getAttributeValue("normal").replaceAll(" ", "").split(",");
        this.normal = new Vector(Double.parseDouble(origin[0]) * Double.parseDouble(normal[0]), 
                Double.parseDouble(origin[1]) * Double.parseDouble(normal[0]), 
                Double.parseDouble(origin[2]) * Double.parseDouble(normal[0]));
    }
    
    public RegionModule getBase() {
        return base;
    }

    public Vector getNormal() {
        return normal;
    }
}
