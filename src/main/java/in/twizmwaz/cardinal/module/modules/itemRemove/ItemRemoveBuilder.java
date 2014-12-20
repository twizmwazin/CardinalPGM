package in.twizmwaz.cardinal.module.modules.itemRemove;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemRemoveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        Set<Material> materials = new HashSet<>(128);
        for (Element itemRemove : match.getDocument().getRootElement().getChildren("itemremove")) {
            for (Element item : itemRemove.getChildren("item")) {
                materials.add(StringUtils.convertStringToMaterial(item.getText()));
            }
        }
        results.add(new ItemRemove(materials));
        return results;
    }

}
