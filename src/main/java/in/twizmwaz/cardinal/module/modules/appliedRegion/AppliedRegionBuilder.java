package in.twizmwaz.cardinal.module.modules.appliedRegion;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.BlockBreakRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.BlockPlaceRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.VelocityRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.util.Vector;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.LATE)
public class AppliedRegionBuilder  implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<AppliedRegion> results = new ModuleCollection<AppliedRegion>();
        for (Element regionElement : match.getDocument().getRootElement().getChildren("regions")) {
            for (Element applied : regionElement.getChildren("apply")) {
                RegionModule region;
                if (applied.getAttributeValue("region") != null) {
                    region = RegionModuleBuilder.getRegion(applied.getAttributeValue("region"));
                } else {
                    ModuleCollection<RegionModule> regions = new ModuleCollection<RegionModule>();
                    for (Element element : applied.getChildren()) {
                        regions.add(RegionModuleBuilder.getRegion(element));
                    }
                    region = new UnionRegion(null, regions);
                }
                String message = applied.getAttributeValue("message");
                if (applied.getAttributeValue("velocity") != null) {
                    String[] velocity = applied.getAttributeValue("velocity").replaceAll("@", "").split(",");
                    results.add(new VelocityRegion(region, new Vector(Double.parseDouble(velocity[0]), 
                            Double.parseDouble(velocity[1]), Double.parseDouble(velocity[2]))));
                }
                if (applied.getAttributeValue("block") != null) {
                    results.add(new BlockPlaceRegion(region, FilterModuleBuilder.getFilter(applied.getAttributeValue("block")), message));
                    results.add(new BlockBreakRegion(region, FilterModuleBuilder.getFilter(applied.getAttributeValue("block")), message));
                } else {
                    if (applied.getAttributeValue("block-place") != null) {
                        results.add(new BlockPlaceRegion(region, FilterModuleBuilder.getFilter(applied.getAttributeValue("block-place")), message));
                    }
                    if (applied.getAttributeValue("block-break") != null) {
                        results.add(new BlockBreakRegion(region, FilterModuleBuilder.getFilter(applied.getAttributeValue("block-break")), message));
                    }
                }
            }
        }
        return results;
    }
}
