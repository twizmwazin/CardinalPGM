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
        ModuleCollection<Blockdrops> results = new ModuleCollection<>();
        for (Element element : match.getDocument().getRootElement().getChildren("blockdrops")) {
            for (Element rule : element.getChildren("rule")) {
                RegionModule region = null;
                if (rule.getChild("region") != null) {
                    region = RegionModuleBuilder.getRegion(rule.getChild("region"));
                }
                FilterModule filter = null;
                if (rule.getChild("filter") != null) {
                    filter = FilterModuleBuilder.getFilter(rule.getChild("filter").getChildren().get(0));
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
                boolean wrongTool = false;
                for (Element wrongToolElement : rule.getChildren("wrongtool")) {
                    wrongTool = Boolean.parseBoolean(wrongToolElement.getText());
                }
                double fallChance = 0.0;
                if (rule.getChild("fall-chance") != null) {
                    fallChance = Double.parseDouble(rule.getChild("fall-chance").getText());
                }
                double landChance = 0.0;
                if (rule.getChild("land-chance") != null) {
                    landChance = Double.parseDouble(rule.getChild("land-chance").getText());
                }
                double fallSpeed = 0.0;
                if (rule.getChild("fall-speed") != null) {
                    fallSpeed = Double.parseDouble(rule.getChild("fall-speed").getText());
                }
                results.add(new Blockdrops(region, filter, drops, replace, experience, wrongTool, fallChance, landChance, fallSpeed));
            }

        }
        return results;
    }
}