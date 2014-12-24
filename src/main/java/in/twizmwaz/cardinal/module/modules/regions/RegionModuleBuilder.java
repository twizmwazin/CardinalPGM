package in.twizmwaz.cardinal.module.modules.regions;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.regions.Region;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionModuleBuilder implements ModuleBuilder {


    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        Map<String, Region> regions = new HashMap<String, Region>();
        try {
            for (Element element : match.getDocument().getRootElement().getChildren("regions")) {
                for (Element regionElement : element.getChildren()) {
                    if (regionElement.getName().equalsIgnoreCase("apply")) break;
                    regions.put(regionElement.getAttributeValue("name"), Region.getRegion(regionElement));
                }
            }
        } catch (NullPointerException e) {

        }

        return results;
    }
}
