package in.twizmwaz.cardinal.module.modules.blood;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Blood implements Module {

    protected Blood() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    /* @EventHandler(priority = EventPriority.MONITOR)
    public void onBlood(EntityDamageByEntityEvent event) {
        if (!event.isCancelled()) {
            for (Player player : Bukkit.getOnlinePlayers()) {

            }
        }
    } */

}
