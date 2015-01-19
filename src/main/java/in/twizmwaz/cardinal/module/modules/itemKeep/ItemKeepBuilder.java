package in.twizmwaz.cardinal.module.modules.itemKeep;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class ItemKeepBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        Set<Material> materials = new HashSet<>(128);
        for (Element itemKeep : match.getDocument().getRootElement().getChildren("itemkeep")) {
            for (Element item : itemKeep.getChildren("item")) {
               materials.add(StringUtils.convertStringToMaterial(item.getText()));
                String[] broken = item.getText().split(":");
                short damage = -1;
                try {
                    damage = Short.parseShort(broken[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                } catch (NumberFormatException e) {
                }
                results.add(new ItemKeep(new KeptItem(materials, damage)));
            }
        }

        return results;
    }

}
