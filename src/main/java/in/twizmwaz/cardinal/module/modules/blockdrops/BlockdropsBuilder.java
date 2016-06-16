package in.twizmwaz.cardinal.module.modules.blockdrops;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterModuleBuilder;
import in.twizmwaz.cardinal.module.modules.filter.parsers.BlockFilterParser;
import in.twizmwaz.cardinal.module.modules.kit.KitBuilder;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.RegionModuleBuilder;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Parser;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BlockdropsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Blockdrops> load(Match match) {
        ModuleCollection<Blockdrops> results = new ModuleCollection<>();
        List<Element> blockDrops = new ArrayList<>();
        blockDrops.addAll(match.getDocument().getRootElement().getChildren("blockdrops"));
        blockDrops.addAll(match.getDocument().getRootElement().getChildren("block-drops"));

        for (Element blockDrop : blockDrops) {
            for (Element rule : blockDrop.getChildren("rule")) {
                results.add(parseRule(rule, blockDrop));
            }
            for (Element blockDrop2 : blockDrop.getChildren()) {
                for (Element rule : blockDrop2.getChildren("rule")) {
                    results.add(parseRule(rule, blockDrop2, blockDrop));
                }
            }
        }
        return results;
    }

    private Blockdrops parseRule(Element... elements) {
        RegionModule region = RegionModuleBuilder.getAttributeOrChild("region", "always", elements);
        FilterModule filter = FilterModuleBuilder.getAttributeOrChild("filter", "always", elements);
        KitNode kit = null;
        if (Parser.getOrderedAttribute("kit", elements) != null) kit = KitNode.getKitByName(Parser.getOrderedAttribute("kit", elements));
        if (elements[0].getChild("kit") != null) kit = KitBuilder.getKit(elements[0].getChild("kit"));
        Map<ItemStack, Double> drops = new HashMap<>();
        for (Element items : elements[0].getChildren("drops")) {
            for (Element item : items.getChildren("item")) {
                drops.put(Parser.getItem(item), (item.getAttributeValue("chance") != null ? Numbers.parseDouble(item.getAttributeValue("chance")) : 1));
            }
        }
        Material replaceType = Material.AIR;
        int replaceDamage = -1;
        for (Element replaceElement : elements[0].getChildren("replacement")) {
            String material = replaceElement.getText();
            String materialType = material.split(":")[0].trim();
            replaceType = (NumberUtils.isNumber(materialType) ? Material.getMaterial(Integer.parseInt(materialType)) : Material.matchMaterial(materialType));
            replaceDamage = material.contains(":") ? Numbers.parseInt(material.split(":")[1].trim()) : -1;
        }
        int experience = elements[0].getChild("experience") != null ? Numbers.parseInt(elements[0].getChild("experience").getText()) : 0;
        boolean wrongTool = Numbers.parseBoolean(Parser.getOrderedAttributeOrChild("wrongtool", elements), false);
        boolean punch = Numbers.parseBoolean(Parser.getOrderedAttributeOrChild("punch", elements), false);
        boolean trample = Numbers.parseBoolean(Parser.getOrderedAttributeOrChild("trample", elements), false);
        double fallChance = Numbers.parseDouble(Parser.getOrderedAttributeOrChild("fall-chance", elements), 0);
        double landChance = Numbers.parseDouble(Parser.getOrderedAttributeOrChild("land-chance", elements), 0);
        double fallSpeed = Numbers.parseDouble(Parser.getOrderedAttributeOrChild("time-multiplier", elements), 0);

        return new Blockdrops(region, filter, kit, drops, replaceType, replaceDamage, experience, wrongTool, punch, trample, fallChance, landChance, fallSpeed);
    }


}