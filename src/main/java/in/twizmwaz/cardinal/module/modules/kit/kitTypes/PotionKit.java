package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.KitRemovable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class PotionKit implements KitRemovable {

    private List<PotionEffect> effects;

    public PotionKit(List<PotionEffect> effects) {
        this.effects = effects;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        if(force) {
            for(PotionEffect effect : effects) player.addPotionEffect(effect, true);
        } else {
            player.addPotionEffects(effects);
        }
    }

    @Override
    public void remove(Player player) {
        for(PotionEffect effect : effects) player.removePotionEffect(effect.getType());
    }

}
