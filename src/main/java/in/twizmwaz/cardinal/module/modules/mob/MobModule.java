package in.twizmwaz.cardinal.module.modules.mob;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobModule implements Module {
    
    private final FilterModule filter;

    protected MobModule(FilterModule filter) {
        this.filter = filter;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (filter.evaluate(event.getSpawnReason(), event.getCreatureType(), event.getEntity().getType()).equals(FilterState.DENY))
            event.setCancelled(true);
    }
}
