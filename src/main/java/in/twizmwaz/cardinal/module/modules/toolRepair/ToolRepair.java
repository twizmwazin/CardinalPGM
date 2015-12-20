package in.twizmwaz.cardinal.module.modules.toolRepair;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ToolRepair implements Module {

    private final Set<Material> materials;

    protected ToolRepair(final Set<Material> materials) {
        this.materials = materials;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!event.isCancelled()) {
            ItemStack item1 = event.getItem().getItemStack();
            if (materials.contains(item1.getType()) && event.getPlayer().getInventory().contains(item1.getType())) {
                for (ItemStack item2 : event.getPlayer().getInventory().getContents()) {
                    if (item2 != null && toMaxDurability(item1).equals(toMaxDurability(item2))) {
                        event.setCancelled(true);
                        event.getItem().remove();
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_PICKUP, 0.1F, 1);
                        int result = item2.getDurability() - (item1.getType().getMaxDurability() - item1.getDurability());
                        item2.setDurability((short) (result < 0 ? 0 : result));
                        break;
                    }
                }
            }
        }
    }

    public ItemStack toMaxDurability(ItemStack item) {
        ItemStack item2 = new ItemStack(item);
        item2.setDurability(item.getType().getMaxDurability());
        return item2;
    }
}
