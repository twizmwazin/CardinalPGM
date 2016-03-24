package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.KitNode;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.KitItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class KillReward implements Module {

    private final KitItem item;
    private final KitNode kit;
    private final FilterModule filter;

    protected KillReward(KitItem item, KitNode kit, final FilterModule filter) {
        this.item = item;
        this.kit = kit;
        this.filter = filter;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && event.getKiller().getHealth() > 0) {
            Player killer = event.getKiller();
            if (!filter.evaluate(killer).equals(FilterState.DENY)) {
                if (kit != null) kit.apply(killer, null);
                if (item != null) {
                    if (!item.hasSlot() || killer.getInventory().getItem(item.getSlot()) != null) {
                        killer.getInventory().addItem(item.getItem());
                    } else {
                        killer.getInventory().setItem(item.getSlot(), item.getItem());
                    }
                }
            }
        }
    }

}
