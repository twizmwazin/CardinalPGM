package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class KitApplier implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        if (event.getSpawn().getKit() != null) event.getSpawn().getKit().apply(event.getPlayer());
    }

}
