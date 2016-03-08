package in.twizmwaz.cardinal.module.modules.multitrade;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Multitrade implements Module {
    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void handleRightClick(PlayerInteractEntityEvent event) {
        if ((event.getRightClicked() instanceof Villager)) {
            event.setCancelled(true);
            event.getPlayer().openMerchantCopy((Villager)event.getRightClicked());
        }
    }

}
