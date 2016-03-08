package in.twizmwaz.cardinal.module.modules.potionRemover;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PotionRemover implements Module {

    protected PotionRemover() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.POTION)) {
            event.setReplacement(new ItemStack(Material.AIR));
        }
    }

}