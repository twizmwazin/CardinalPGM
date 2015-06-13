package in.twizmwaz.cardinal.module.modules.appliedRegion;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.BlockBreakRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.BlockEventRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.BlockPlaceAgainstRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.BlockPlaceRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.EnterRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.KitRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.LeaveRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.UseRegion;
import in.twizmwaz.cardinal.module.modules.appliedRegion.type.VelocityRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.type.logic.AllFilter;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.portal.Portal;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import org.bukkit.ChatColor;
import org.bukkit.util.Vector;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.LATE)
public class AppliedRegionBuilder implements ModuleBuilder {

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
                String message = (applied.getAttributeValue("message") != null ? ChatColor.translateAlternateColorCodes('`', applied.getAttributeValue("message")) : null);
                if (applied.getAttributeValue("velocity") != null) {
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
                if (applied.getAttributeValue("block-place-against") != null) {
                    results.add(new BlockPlaceAgainstRegion(region, getFilter(applied.getAttributeValue("block-place-against")), message));
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
                if (applied.getAttributeValue("use") != null) {
                    results.add(new UseRegion(region, getFilter(applied.getAttributeValue("use")), message));
                }
                if (applied.getAttributeValue("kit") != null) {
                    results.add(new KitRegion(region, (applied.getAttributeValue("filter") != null ? getFilter(applied.getAttributeValue("filter")) : null), message, Kit.getKitByName(applied.getAttributeValue("kit"))));
                }
            }
        }
        for (Portal portal : match.getModules().getModules(Portal.class)) {
            if (portal.getDestination() != null) {
                results.add(new BlockPlaceRegion(portal.getDestination(), getFilter("deny-all"), null));
            }
            if (portal.protect()) {
                results.add(new BlockEventRegion(portal.getRegion(), getFilter("deny-all"), null));
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
