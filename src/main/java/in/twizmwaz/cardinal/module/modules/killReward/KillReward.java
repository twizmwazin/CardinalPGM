package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.KitCollection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class KillReward implements Module {

    private final ModuleCollection<KitCollection> kits;
    private final ModuleCollection<FilterModule> filters;

    protected KillReward(final ModuleCollection<KitCollection> kits, final ModuleCollection<FilterModule> filters) {
        this.kits = kits;
        this.filters = filters;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && event.getKiller().getHealth() > 0) {
            Player killer = event.getKiller();
            boolean proceed = true;
            for (FilterModule filter : this.filters) {
                if (filter.evaluate(killer).equals(FilterState.DENY)) {
                    proceed = false;
                }
            }
            if (proceed) {
                for (KitCollection kit : kits) {
                    kit.apply(killer, null);
                }
            }
        }
    }

}
