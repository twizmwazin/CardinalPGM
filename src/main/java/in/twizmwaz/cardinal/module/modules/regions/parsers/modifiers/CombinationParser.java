package in.twizmwaz.cardinal.module.modules.regions.parsers.modifiers;

import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionParser;
import org.jdom2.Document;
import org.jdom2.Element;

public class CombinationParser extends RegionParser {

    private final String name;
    private final ModuleCollection<RegionModule> regions = new ModuleCollection<>();

    public CombinationParser(Element element, Document document) {
        super(element.getAttributeValue("name"));
        this.name = element.getAttributeValue("name");
        for (Element child : element.getChildren()) regions.add(RegionModuleBuilder.getRegion(child, document));
    }

    public ModuleCollection<RegionModule> getRegions() {
        return regions;
    }

}
