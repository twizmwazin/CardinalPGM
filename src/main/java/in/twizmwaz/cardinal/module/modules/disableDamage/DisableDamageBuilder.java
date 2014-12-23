package in.twizmwaz.cardinal.module.modules.disableDamage;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisableDamageBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        Set<DamageCause> damageTypes = new HashSet<>(128);
        for (Element itemRemove : match.getDocument().getRootElement().getChildren("disabledamage")) {
            for (Element item : itemRemove.getChildren("damage")) {
                damageTypes.add(DamageCause.valueOf(item.getText().toUpperCase().replaceAll(" ", "_")));        //Remember to check for custom tags later after TNT tracking implementation
            }
        }
        results.add(new DisableDamage(damageTypes));
        return results;
    }

}
