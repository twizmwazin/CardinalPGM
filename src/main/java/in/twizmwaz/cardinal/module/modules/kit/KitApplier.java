package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.respawn.RespawnModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class KitApplier implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onObserverSpawn(CardinalSpawnEvent event) {
        if (!GameHandler.getGameHandler().getMatch().isRunning() || event.getTeam().isObserver())
            RespawnModule.giveObserversKit(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCardinalSpawn(CardinalSpawnEvent event) {
        final Kit kit = event.getSpawn().getKit();
        final Player player = event.getPlayer();
        if (kit != null) {
            kit.apply(player, null);
        }
        event.getPlayer().updateInventory();
    }

}
