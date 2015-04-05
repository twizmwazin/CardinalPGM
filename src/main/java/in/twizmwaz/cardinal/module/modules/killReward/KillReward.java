package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class KillReward implements Module {

    private final ModuleCollection<Kit> kits;
    private final ModuleCollection<FilterModule> filters;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    protected KillReward(final ModuleCollection<Kit> kits, final ModuleCollection<FilterModule> filters) {
        this.kits = kits;
        this.filters = filters;
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
                for (Kit kit : kits) {
                    kit.apply(killer);
                }
            }
        }
    }

}
