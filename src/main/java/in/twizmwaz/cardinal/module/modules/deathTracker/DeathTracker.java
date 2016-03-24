package in.twizmwaz.cardinal.module.modules.deathTracker;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class DeathTracker implements Module {

    protected DeathTracker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(CardinalDeathEvent event) {
        TrackerDamageEvent tracker = DamageTracker.getEvent(event.getPlayer());
        boolean time = tracker != null && System.currentTimeMillis() - tracker.getTime() <= 7500;
        if (time) {
            if (event.getTrackerDamageEvent() == null) {
                event.setTrackerDamageEvent(tracker);
            }
            if (event.getKiller() == null && tracker.getDamager() != null) {
                event.setKiller(tracker.getDamager().getPlayer());
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == event.getPlayer()) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1);
            } else if (event.getKiller() != null && player == event.getKiller()) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1.35F);
            } else {
                player.playSound(event.getPlayer().getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 1, 1.35F);
            }
        }
    }

}