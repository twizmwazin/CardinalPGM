package in.twizmwaz.cardinal.module.modules.killReward;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillReward implements Module {

    private final Kit kit;
    private final ModuleCollection<FilterModule> filters;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    protected KillReward(final Kit kit, final ModuleCollection<FilterModule> filters) {
        this.kit = kit;
        this.filters = filters;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            boolean proceed = true;
            for (FilterModule filter : this.filters) {
                if (filter.evaluate(killer).equals(FilterState.DENY)) {
                    proceed = false;
                }
            }
            if (proceed)
                this.kit.apply(killer);
        } else {
            try {
                EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
                if (cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                        EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
                        if (TntTracker.getWhoPlaced(damageByEntityEvent.getDamager()) != null) {
                            if (Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(damageByEntityEvent.getDamager())).isOnline()) {
                                Player source = Bukkit.getPlayer(TntTracker.getWhoPlaced(damageByEntityEvent.getDamager()));
                                if (!source.equals(event.getEntity())) {
                                    boolean proceed = true;
                                    for (FilterModule filter : this.filters) {
                                        if (filter.evaluate(source).equals(FilterState.DENY)) {
                                            proceed = false;
                                        }
                                    }
                                    if (proceed)
                                        this.kit.apply(source);
                                }
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

}
