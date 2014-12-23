package in.twizmwaz.cardinal.module.modules.disableDamage;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Set;

public class DisableDamage implements Module {

    private final Set<DamageCause> damageTypes;

    protected DisableDamage(final Set<DamageCause> damageTypes) {
        this.damageTypes = damageTypes;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (damageTypes.contains(event.getCause())) {
            event.setCancelled(true);
        }
    }
}
