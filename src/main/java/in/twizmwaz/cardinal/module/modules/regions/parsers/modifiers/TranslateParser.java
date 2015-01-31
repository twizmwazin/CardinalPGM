package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.Bukkit;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.jdom2.Element;

public class TranslateParser extends RegionParser {

    private final RegionModule base;
    private final Vector offset;

    public TranslateParser(Element element) {
        super(element.getAttributeValue("name"));
        this.base = element.getAttributeValue("region") != null ? RegionModuleBuilder.getRegion(element.getAttributeValue("region")) : RegionModuleBuilder.getRegion(element.getChildren().get(0));
        String[] offset = element.getAttributeValue("offset").contains(",") ?  element.getAttributeValue("offset").split(",") : element.getAttributeValue("offset").trim().replaceAll(" ",",").split(",");
        this.offset = new BlockVector(NumUtils.parseDouble(offset[0].trim()), NumUtils.parseDouble(offset[1].trim()), NumUtils.parseDouble(offset[2].trim()));
    }

    public RegionModule getBase() {
        return base;
    }

    public Vector getOffset() {
        return offset;
        
    }
}
