package in.twizmwaz.cardinal.module.modules.deathTracker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.module.modules.tracker.DamageTracker;
import in.twizmwaz.cardinal.module.modules.tracker.SpleefTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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
            Player killer = event.getEntity().getKiller();
            try {
                EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
                if (cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                        if (TntTracker.getWhoPlaced(damageByEntityEvent.getDamager()) != null) {
                            if (Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(damageByEntityEvent.getDamager())).isOnline()) {
                                killer = Bukkit.getPlayer(TntTracker.getWhoPlaced(damageByEntityEvent.getDamager()));
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
            }
            if (killer == null && SpleefTracker.getLastSpleefEvent(event.getEntity()) != null && (System.currentTimeMillis() - SpleefTracker.getLastSpleefEvent(event.getEntity()).getTime() <= 10000) && SpleefTracker.getLastSpleefEvent(event.getEntity()).getSpleefer().isOnline())
                killer = (Player) SpleefTracker.getLastSpleefEvent(event.getEntity()).getSpleefer();
            CardinalDeathEvent deathEvent = new CardinalDeathEvent(event.getEntity(), killer);
            if (DamageTracker.getLastDamageEvent(event.getEntity()) != null && event.getEntity().getKiller() != null)
                deathEvent.setTrackerDamageEvent(DamageTracker.getLastDamageEvent(event.getEntity()));
            if (SpleefTracker.getLastSpleefEvent(event.getEntity()) != null && (System.currentTimeMillis() - SpleefTracker.getLastSpleefEvent(event.getEntity()).getTime() <= 10000))
                deathEvent.setPlayerSpleefEvent(SpleefTracker.getLastSpleefEvent(event.getEntity()));
            Bukkit.getServer().getPluginManager().callEvent(deathEvent);
        } else {
            event.getEntity().removeMetadata("teamChange", GameHandler.getGameHandler().getPlugin());
        }
        event.setDeathMessage(null);
    }
}