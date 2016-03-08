package in.twizmwaz.cardinal.module.modules.deathTracker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import in.twizmwaz.cardinal.module.modules.tracker.Type;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DeathTracker implements Module {

    protected DeathTracker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!event.getEntity().hasMetadata("teamChange")) {
            Player killer = null;
            TrackerDamageEvent tracker = DamageTracker.getEvent(event.getEntity());
            boolean time = tracker != null && System.currentTimeMillis() - tracker.getTime() <= 7500;
            if (tracker != null && (tracker.getType().equals(Type.KNOCKED) || tracker.getType().equals(Type.SHOT)) && event.getEntity().getKiller() != null && event.getEntity().getKiller().equals(tracker.getDamager())) {
                killer = tracker.getDamager().getPlayer();
            } else if (time) {
                killer = tracker.getDamager().getPlayer();
            }
            CardinalDeathEvent deathEvent = new CardinalDeathEvent(event.getEntity(), killer);
            if (time && tracker.getDamager().getPlayer() != null) {
                deathEvent.setTrackerDamageEvent(tracker);
            }
            Bukkit.getServer().getPluginManager().callEvent(deathEvent);
        } else {
            event.getEntity().removeMetadata("teamChange", GameHandler.getGameHandler().getPlugin());
        }
        event.setDeathMessage(null);
    }

}