package in.twizmwaz.cardinal.module.modules.kit.kitTypes;


import in.twizmwaz.cardinal.module.modules.kit.KitRemovable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class KnockbackReductionKit implements KitRemovable {

    float knockbackReduction;

    public KnockbackReductionKit(float knockbackReduction) {
        this.knockbackReduction = knockbackReduction;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        player.setKnockbackReduction(knockbackReduction);
    }

    @Override
    public void remove(Player player) {
        player.setKnockbackReduction(0);
    }

}
