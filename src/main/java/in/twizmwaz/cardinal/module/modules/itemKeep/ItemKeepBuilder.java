package in.twizmwaz.cardinal.module.modules.itemKeep;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.NumUtils;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class ItemKeepBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        Set<Material> materials = new HashSet<>(128);
        for (Element itemKeep : match.getDocument().getRootElement().getChildren("itemkeep")) {
            for (Element item : itemKeep.getChildren("item")) {
                Material material;
                int damageValue = 0;
                if (item.getText().contains(":")) {
                    material = Material.matchMaterial(item.getText().split(":")[0]);
                    damageValue = NumUtils.parseInt(item.getText().split(":")[1]);
                } else {
                    material = Material.matchMaterial(item.getText());
                }
                if (item.getAttributeValue("damage") != null) {
                    damageValue = NumUtils.parseInt(item.getAttributeValue("damage"));
                }
                results.add(new ItemKeep(material, damageValue));
            }
        }

        return results;
    }

}