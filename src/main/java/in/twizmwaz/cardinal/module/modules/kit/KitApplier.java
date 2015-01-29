package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class KitApplier implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPgmSpawn(PgmSpawnEvent event) {
        try {
            Kit kit = null;
            for (Kit kitModule : GameHandler.getGameHandler().getMatch().getModules().getModules(Kit.class)) {
                if (kitModule.getName() != null && kitModule.getName().equalsIgnoreCase(event.getSpawn().getKit())) {
                    kit = kitModule;
                }
            }
            kit.apply(event.getPlayer());
        } catch (NullPointerException e) {
        }
    }

}
