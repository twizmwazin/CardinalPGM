package in.twizmwaz.cardinal.module.modules.blood;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.HandlerList;

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
