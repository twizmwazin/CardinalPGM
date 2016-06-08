package in.twizmwaz.cardinal.module.modules.scorebox;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

public class ScoreboxBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Scorebox> load(Match match) {
        ModuleCollection<Scorebox> results = new ModuleCollection<>();
        for (Element score : match.getDocument().getRootElement().getChildren("score")) {
            for (Element box : score.getChildren("box")) {
                RegionModule region = RegionModuleBuilder.getAttributeOrChild("region", RegionModuleBuilder.getRegion(box), box);
                int points = Numbers.parseInt(Parser.getOrderedAttribute("points", box), Numbers.parseInt(box.getAttributeValue("value"), 0));
                FilterModule filter = FilterModuleBuilder.getAttributeOrChild("filter", box);
                Map<ItemStack, Integer> redeemables = new HashMap<>();
                boolean silent = Numbers.parseBoolean(Parser.getOrderedAttribute("silent", box), Numbers.parseBoolean(box.getAttributeValue("silent"), false));
                for (Element child : box.getChildren("redeemables")) {
                    for (Element item : child.getChildren("item")) {
                        redeemables.put(Parser.getItem(item), (item.getAttributeValue("points") != null ? Numbers.parseInt(item.getAttributeValue("points")) : (item.getAttributeValue("value") != null ? Numbers.parseInt(item.getAttributeValue("value")) : 0)));
                    }
                }
                results.add(new Scorebox(region, points, filter, silent, redeemables));
            }
        }
        return results;
    }

}
