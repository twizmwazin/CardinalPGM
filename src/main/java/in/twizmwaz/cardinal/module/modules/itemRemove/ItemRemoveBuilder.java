package in.twizmwaz.cardinal.module.modules.itemRemove;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.bukkit.Material;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class ItemRemoveBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        for (Element itemRemove : match.getDocument().getRootElement().getChildren("itemremove")) {
            for (Element item : itemRemove.getChildren("item")) {
                String[] broken = item.getText().split(":");
                Material material;
                try {
                    material = Material.getMaterial(Integer.parseInt(broken[0]));
                } catch (NumberFormatException e) {
                    material = Material.matchMaterial(broken[0]);
                }
                short damage = -1;
                try {
                    damage = Short.parseShort(broken[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                } catch (NumberFormatException e) {
                }
                results.add(new ItemRemove(new RemovedItem(material, damage)));
            }
        }
        return results;
    }

}
