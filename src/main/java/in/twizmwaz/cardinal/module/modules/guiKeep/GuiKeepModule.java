package in.twizmwaz.cardinal.module.modules.guiKeep;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GuiKeepModule implements Module {

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !ObserverModule.testObserverOrDead(event.getPlayer())) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getClickedBlock() != null) {
                    if (event.getClickedBlock().getType().equals(Material.WORKBENCH)) {
                        if (!event.getPlayer().isSneaking() || event.getPlayer().getItemInHand() == null) {
                            event.setCancelled(true);
                            event.getPlayer().openWorkbench(event.getPlayer().getLocation(), true);
                        }
                    }
                }
            }
        }
    }

}
