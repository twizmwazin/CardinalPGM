package in.twizmwaz.cardinal.module.modules.projectiles;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class ProjectilesBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        EntityType projectile = EntityType.ARROW;
        double velocityMod = 1.0;
        List<PotionEffect> potionEffects = new ArrayList<>();
        ModuleCollection<Projectiles> results = new ModuleCollection<Projectiles>();
        for (Element projectiles : match.getDocument().getRootElement().getChildren("modifybowprojectile")) {
            try {
                projectile = EntityType.valueOf(Strings.getTechnicalName(projectiles.getChild("projectile").getText()));
            } catch (NullPointerException ex) {

            }
            try {
                velocityMod = Double.parseDouble(projectiles.getChild("velocityMod").getText());
            } catch (NullPointerException ex) {

            }

            for (Element potion : projectiles.getChildren("potion")) {
                potionEffects.add(Parser.getPotion(potion));
            }

        }
        results.add(new Projectiles(projectile, velocityMod, potionEffects));
        return results;
    }

}
