package in.twizmwaz.cardinal.module.modules.appliedRegion;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.*;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.AllFilter;
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
                    for (Element element : applied.getChildren()) regions.add(RegionModuleBuilder.getRegion(element));
                    region = new UnionRegion(null, regions);
                }
                String message = ChatColor.translateAlternateColorCodes('`', applied.getAttributeValue("message"));
                if (applied.getAttributeValue("velocity") != null)  {
                    String[] values = applied.getAttributeValue("velocity").replaceAll("@", "").split(",");
                    FilterModule filter = applied.getAttributeValue("filter") == null ? null : getFilter(applied.getAttributeValue("filter"));
                    Vector velocity = new Vector(Double.parseDouble(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
                    results.add(new VelocityRegion(region, filter, message, velocity));
                }
                if (applied.getAttributeValue("block-break") != null) {
                    results.add(new BlockBreakRegion(region, getFilter(applied.getAttributeValue("block-break")), message));
                }
                if (applied.getAttributeValue("block-place") != null) {
                    results.add(new BlockPlaceRegion(region, getFilter(applied.getAttributeValue("block-place")), message));
                }
                if (applied.getAttributeValue("block") != null) {
                    results.add(new BlockEventRegion(region, getFilter(applied.getAttributeValue("block")), message));
                }
                if (applied.getAttributeValue("enter") != null) {
                    results.add(new EnterRegion(region, getFilter(applied.getAttributeValue("enter")), message));
                }
                if (applied.getAttributeValue("leave") != null) {
                    results.add(new LeaveRegion(region, getFilter(applied.getAttributeValue("leave")), message));
                }
            }
        }
        return results;
    }
    
    private FilterModule getFilter(String string) {
        string = string.trim();
        if (!string.contains(" ")) return FilterModuleBuilder.getFilter(string);
        else {
            ModuleCollection<FilterModule> collection = new ModuleCollection<>();
            for (String filter : string.split(" ")) {
                FilterModule filterModule = FilterModuleBuilder.getFilter(filter);
                if (filterModule != null) collection.add(filterModule);
            }
            return new AllFilter(null, collection);
        }
    } 
}
