package in.twizmwaz.cardinal.module.modules.appliedRegion;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.VelocityRegion;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import org.bukkit.util.Vector;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.LATE)
public class AppliedRegionBuilder  implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<AppliedRegion> results = new ModuleCollection<AppliedRegion>();
        for (Element regionElement : match.getDocument().getRootElement().getChildren("regions")) {
            for (Element applied : regionElement.getChildren("apply")) {
                if (applied.getAttributeValue("velocity") != null) {
                    String[] velocity = applied.getAttributeValue("velocity").replaceAll("@", "").split(",");
                    if (applied.getAttributeValue("region") != null) {
                        results.add(new VelocityRegion(RegionModuleBuilder.getRegion(applied.getAttributeValue("region")), 
                                new Vector(Double.parseDouble(velocity[0]), Double.parseDouble(velocity[1]), Double.parseDouble(velocity[2]))));
                    } else for (Element region : applied.getChildren()) {
                        results.add(new VelocityRegion(RegionModuleBuilder.getRegion(region),
                                new Vector(Double.parseDouble(velocity[0]), Double.parseDouble(velocity[1]), Double.parseDouble(velocity[2]))));
                    }
                }
            }
        }
        return results;
    }
}
