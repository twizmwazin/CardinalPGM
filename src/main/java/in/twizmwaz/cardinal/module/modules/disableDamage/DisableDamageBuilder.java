package in.twizmwaz.cardinal.module.modules.disableDamage;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.jdom2.Element;

import java.util.HashSet;
import java.util.Set;

public class DisableDamageBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        Set<DamageCause> damageTypes = new HashSet<>(128);
        boolean ally = true, self = true, enemy = true, other = true;
        for (Element itemRemove : match.getDocument().getRootElement().getChildren("disabledamage")) {
            for (Element item : itemRemove.getChildren("damage")) {
                damageTypes.add(DamageCause.valueOf(item.getText().toUpperCase().replaceAll(" ", "_")));
                if (DamageCause.valueOf(StringUtils.getTechnicalName(item.getText())) == DamageCause.BLOCK_EXPLOSION) {
                    try {
                        ally = item.getAttributeValue("ally").equalsIgnoreCase("false");
                    } catch (NullPointerException ex) {
                        //Attribute does not exist
                    }
                    try {
                        self = item.getAttributeValue("self").equalsIgnoreCase("false");
                    } catch (NullPointerException ex) {
                        //Attribute does not exist
                    }
                    try {
                        enemy = item.getAttributeValue("enemy").equalsIgnoreCase("false");
                    } catch (NullPointerException ex) {
                        //Attribute does not exist
                    }
                    try {
                        other = item.getAttributeValue("other").equalsIgnoreCase("false");
                    } catch (NullPointerException ex) {
                        //Attribute does not exist
                    }
                }
            }
        }
        DisableDamage disableDamage = new DisableDamage(damageTypes);
        disableDamage.setBlockExplosionAlly(ally);
        disableDamage.setBlockExplosionSelf(self);
        disableDamage.setBlockExplosionEnemy(enemy);
        disableDamage.setBlockExplosionOther(other);
        results.add(disableDamage);
        return results;
    }

}
