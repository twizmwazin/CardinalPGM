package in.twizmwaz.cardinal.module.modules.armorKeep;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class ArmorKeepBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<ArmorKeep> results = new ModuleCollection<>();
        Set<Material> materials = new HashSet<>(128);
        for (Element itemKeep : match.getDocument().getRootElement().getChildren("armorkeep")) {
            for (Element item : itemKeep.getChildren("item")) {
                Material material;
                int damageValue = 0;
                if (item.getText().contains(":")) {
                    material = Material.matchMaterial(item.getText().split(":")[0]);
                    damageValue = Integer.parseInt(item.getText().split(":")[1]);
                } else {
                    material = Material.matchMaterial(item.getText());
                }
                results.add(new ArmorKeep(material, damageValue));
            }
        }

        return results;
    }

}