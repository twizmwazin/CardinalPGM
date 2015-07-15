package in.twizmwaz.cardinal.module.modules.scorebox;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

import java.util.HashMap;

public class ScoreboxBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Scorebox> load(Match match) {
        ModuleCollection<Scorebox> results = new ModuleCollection<>();
        for (Element score : match.getDocument().getRootElement().getChildren("score")) {
            for (Element box : score.getChildren("box")) {
                RegionModule region;
                if (box.getAttributeValue("region") != null) {
                    region = RegionModuleBuilder.getRegion(box);
                } else {
                    ModuleCollection<RegionModule> queued = new ModuleCollection<>();
                    for (Element child : box.getChildren()) {
                        queued.add(RegionModuleBuilder.getRegion(child));
                    }
                    region = new UnionRegion(null, queued);
                }
                int points = 0;
                if (box.getAttributeValue("points") != null) {
                    points = Numbers.parseInt(box.getAttributeValue("points"));
                } else if (box.getAttributeValue("value") != null) {
                    points = Numbers.parseInt(box.getAttributeValue("value"));
                }
                FilterModule filter = null;
                if (box.getAttributeValue("filter") != null) {
                    filter = FilterModuleBuilder.getFilter(box.getAttributeValue("filter"));
                } else {
                    for (Element child : box.getChildren("filter")) {
                        filter = FilterModuleBuilder.getFilter(child);
                    }
                }
                HashMap<ItemStack, Integer> redeemables = new HashMap<>();
                for (Element child : box.getChildren("redeemables")) {
                    for (Element item : child.getChildren("item")) {
                        redeemables.put(Parser.getItem(item), (item.getAttributeValue("points") != null ? Numbers.parseInt(item.getAttributeValue("points")) : (item.getAttributeValue("value") != null ? Numbers.parseInt(item.getAttributeValue("value")) : 0)));
                    }
                }
                results.add(new Scorebox(region, points, filter, redeemables));
            }
        }
        return results;
    }

}
