package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockdropsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Blockdrops> load(Match match) {
        ModuleCollection<Blockdrops> results = new ModuleCollection<>();
        List<Element> blockdrops = new ArrayList<>();
        blockdrops.addAll(match.getDocument().getRootElement().getChildren("blockdrops"));
        blockdrops.addAll(match.getDocument().getRootElement().getChildren("block-drops"));
        for (Element element : blockdrops) {
            for (Element rule : element.getChildren("rule")) {
                RegionModule region = null;
                if (rule.getChild("region") != null) {
                    region = RegionModuleBuilder.getRegion(rule.getChild("region"));
                }
                FilterModule filter = null;
                if (rule.getChild("filter") != null) {
                    filter = FilterModuleBuilder.getFilter(rule.getChild("filter").getChildren().get(0));
                }
                Set<ItemStack> drops = new HashSet<>();
                for (Element items : rule.getChildren("drops")) {
                    for (Element item : items.getChildren("item")) {
                        drops.add(Parser.getItem(item));
                    }
                }
                Material replaceType = Material.AIR;
                int replaceDamage = -1;
                for (Element replaceElement : rule.getChildren("replacement")) {
                    String material = replaceElement.getText();
                    String materialType = material.split(":")[0].trim();
                    replaceType = (NumberUtils.isNumber(materialType) ? Material.getMaterial(Integer.parseInt(materialType)) : Material.matchMaterial(materialType));
                    replaceDamage = material.contains(":") ? Numbers.parseInt(material.split(":")[1].trim()) : -1;
                }
                int experience = 0;
                for (Element experienceElement : rule.getChildren("experience")) {
                    experience = Numbers.parseInt(experienceElement.getText());
                }
                boolean wrongTool = false;
                for (Element wrongToolElement : rule.getChildren("wrongtool")) {
                    wrongTool = Numbers.parseBoolean(wrongToolElement.getText());
                }
                results.add(new Blockdrops(region, filter, drops, replaceType, replaceDamage, experience, wrongTool));
            }

        }
        return results;
    }
}