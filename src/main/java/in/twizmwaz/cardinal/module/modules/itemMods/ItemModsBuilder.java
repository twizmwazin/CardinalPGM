package in.twizmwaz.cardinal.module.modules.itemMods;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.util.Parser;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

@LoadTime(ModuleLoadTime.EARLIEST)
public class ItemModsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ItemMods> load(Match match) {
        Set<ItemRule> itemRules = new HashSet<>();
        for (Element itemMods : match.getDocument().getRootElement().getChildren("item-mods")) {
            for (Element rule : itemMods.getChildren("rule")) {
                Set<Material> singleMaterials = new HashSet<>();
                Set<Pair<Material, Integer>> materials = new HashSet<>();
                boolean allMaterials = false;
                boolean allBlocks = false;
                for (Element itemMatch : rule.getChild("match").getChildren()) {
                    if (itemMatch.getName().equalsIgnoreCase("all-materials")) allMaterials = true;
                    if (itemMatch.getName().equalsIgnoreCase("all-blocks")) allBlocks = true;
                    if (itemMatch.getName().equalsIgnoreCase("material")) {
                        Pair<Material, Integer> material = Parser.parseMaterial(itemMatch.getText());
                        if (itemMatch.getText().contains(":")) materials.add(material);
                        else singleMaterials.add(material.getKey());
                    }
                }
                itemRules.add(new ItemRule(new ItemMatch(singleMaterials, materials, allMaterials, allBlocks), rule.getChild("modify")));
            }
        }
        return new ModuleCollection<>(new ItemMods(itemRules));
    }

}
