package in.twizmwaz.cardinal.module.modules.deathTracker;

import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import in.twizmwaz.cardinal.module.modules.tracker.Type;
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
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(event.getPlayer().getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1, 1.2F);
        }
        if (event.getKiller() == null) {
            Player killer = null;
            TrackerDamageEvent tracker = DamageTracker.getEvent(event.getPlayer());
            boolean time = tracker != null && System.currentTimeMillis() - tracker.getTime() <= 7500;
            if (tracker != null && (tracker.getType().equals(Type.KNOCKED) || tracker.getType().equals(Type.SHOT)) || time) {
                killer = tracker.getDamager().getPlayer();
            }
            event.setKiller(killer);
        }
    }

}