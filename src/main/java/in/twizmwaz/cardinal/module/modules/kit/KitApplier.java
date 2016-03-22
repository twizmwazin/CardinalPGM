package in.twizmwaz.cardinal.module.modules.kit;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class KitApplier implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPgmSpawn(CardinalSpawnEvent event) {
        final Kit kit = event.getSpawn().getKit();
        final Player player = event.getPlayer();
        if (kit != null && (event.getTeam().isObserver() || GameHandler.getGameHandler().getMatch().isRunning())) {
            Bukkit.getScheduler().runTask(Cardinal.getInstance(), new Runnable() {
                @Override
                public void run() {
                    kit.apply(player, null);
                }
            });
        }
    }

}
