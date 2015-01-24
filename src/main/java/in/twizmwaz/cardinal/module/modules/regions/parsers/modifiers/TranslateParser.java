package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class TranslateParser extends RegionParser{

    private final RegionModule base;
    private final Vector offset;

    public TranslateParser(Element element) {
        super(element.getAttributeValue("name"));
        this.base = element.getChildren().size() > 0 ? 
                RegionModuleBuilder.getRegion(element.getChildren().get(0)) : 
                RegionModuleBuilder.getRegion(element.getAttributeValue("region"));
        String[] offset = element.getAttributeValue("offset").trim().replaceAll(" ", ",").split(",");
        this.offset = new Vector(Double.parseDouble(offset[1]), Double.parseDouble(offset[2]), Double.parseDouble(offset[3]));
    }

    public RegionModule getBase() {
        return base;
    }

    public Vector getOffset() {
        return offset;
        
    }
}
