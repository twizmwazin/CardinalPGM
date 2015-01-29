package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.bukkit.util.*;
import org.jdom2.Element;

public class TranslateParser extends RegionParser{

    private final RegionModule base;
    private final Vector offset;

    public TranslateParser(Element element) {
        super(element.getAttributeValue("name"));
        this.base = element.getChildren().size() > 0 ? 
                RegionModuleBuilder.getRegion(element.getChildren().get(0)) : 
                RegionModuleBuilder.getRegion(element.getAttributeValue("region"));
        String[] offset = element.getAttributeValue("offset").contains(",") ?  element.getAttributeValue("offset").split(",") :
                element.getAttributeValue("offset").trim().replaceAll(" ","").split(",");
        this.offset = new BlockVector(Double.parseDouble(offset[0].trim()),
                Double.parseDouble(offset[1].trim()),
                Double.parseDouble(offset[2].trim()));
    }

    public RegionModule getBase() {
        return base;
    }

    public Vector getOffset() {
        return offset;
        
    }
}
