package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.util.ParseUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class BlockdropsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("blockdrops")) {
            for (Element rule : element.getChildren("rule")) {
                RegionModule region = null;
                for (Element regionElement : rule.getChildren("region")) {
                    ModuleCollection<RegionModule> regions = new ModuleCollection<RegionModule>();
                    regions.add(RegionModuleBuilder.getRegion(regionElement));
                    region = new UnionRegion(null, regions);
                }
                ModuleCollection<FilterModule> filters = new ModuleCollection<>();
                for (Element filter : rule.getChildren("filter")) {
                    filters.add(FilterModuleBuilder.getFilter(filter.getChildren().get(0)));
                }
                Set<ItemStack> drops = new HashSet<ItemStack>(128);
                for (Element items : rule.getChildren("drops")) {
                    for (Element item : items.getChildren("item")) {
                        drops.add(ParseUtils.getItem(item));
                    }
                }
                Material replace = Material.AIR;
                for (Element replaceElement : rule.getChildren("replacement")) {
                    replace = Material.matchMaterial(replaceElement.getText());
                }
                int experience = 0;
                for (Element experienceElement : rule.getChildren("experience")) {
                    experience = Integer.parseInt(experienceElement.getText());
                }
                boolean wrongtool = false;
                for (Element wrongtoolElement : rule.getChildren("wrongtool")) {
                    wrongtool = Boolean.parseBoolean(wrongtoolElement.getText());
                }
                results.add(new Blockdrops(region, filters, drops, replace, experience, wrongtool));
            }

        }
        return results;
    }
}