package in.twizmwaz.cardinal.module.modules.armorKeep;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.Numbers;
import org.bukkit.Material;
import org.jdom2.Element;

public class ArmorKeepBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ArmorKeep> load(Match match) {
        ModuleCollection<ArmorKeep> results = new ModuleCollection<>();
        for (Element itemKeep : match.getDocument().getRootElement().getChildren("armorkeep")) {
            for (Element item : itemKeep.getChildren("item")) {
                Material material;
                int damageValue = 0;
                if (item.getText().contains(":")) {
                    material = Material.matchMaterial(item.getText().split(":")[0]);
                    damageValue = Numbers.parseInt(item.getText().split(":")[1]);
                } else {
                    material = Material.matchMaterial(item.getText());
                }
                results.add(new ArmorKeep(material, damageValue));
            }
        }

        return results;
    }

}