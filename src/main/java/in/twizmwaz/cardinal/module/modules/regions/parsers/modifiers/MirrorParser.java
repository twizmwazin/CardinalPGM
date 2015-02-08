package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class MirrorParser extends RegionParser {

    private RegionModule base;
    private final Vector origin, normal;

    public MirrorParser(Element element) {
        super(element.getAttributeValue("name"));
        try {
            this.base = RegionModuleBuilder.getRegion(element.getChildren().get(0));
        } catch (IndexOutOfBoundsException e) {
            for (RegionModule regionModule : GameHandler.getGameHandler().getMatch().getModules().getModules(RegionModule.class)) {
                if (regionModule.getName() != null && regionModule.getName().equalsIgnoreCase(element.getAttributeValue("region"))) this.base = regionModule;
            }
        }
        if (element.getAttributeValue("origin") != null) {
            String[] origin = element.getAttributeValue("origin").contains(",") ? element.getAttributeValue("origin").split(",") :
                    element.getAttributeValue("origin").trim().replaceAll(" ", ",").split(",");
            this.origin = new BlockVector(Double.parseDouble(origin[0]), Double.parseDouble(origin[1]), Double.parseDouble(origin[2]));
        } else {
            this.origin = new Vector(0, 0, 0);
        }
        String[] normal = element.getAttributeValue("normal").trim().replaceAll(" ", ",").split(",");
        this.normal = new BlockVector(Double.parseDouble(normal[0]), Double.parseDouble(normal[1]), Double.parseDouble(normal[2]));
    }
    
    public RegionModule getBase() {
        return base;
    }

    public Vector getOrigin() {
        return origin;
    }

    public Vector getNormal() {
        return normal;
    }
}
