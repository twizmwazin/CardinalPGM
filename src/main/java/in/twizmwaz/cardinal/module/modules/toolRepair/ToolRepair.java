package in.twizmwaz.cardinal.module.modules.toolRepair;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
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
            if (materials.contains(item1.getType())) {
                if (event.getPlayer().getInventory().contains(item1.getType())) {
                    ItemStack toRepair = null;
                    boolean itemsMatch = false;
                    for (ItemStack item2 : event.getPlayer().getInventory().getContents()) {
                        if (item2 != null) {
                            if (item1.getType() == item2.getType()) {
                                if (item1.getEnchantments() != null && item2.getEnchantments() != null) {
                                    if (item1.getEnchantments().equals(item2.getEnchantments())) {
                                        itemsMatch = true;
                                    }
                                } else {
                                    if (item1.getEnchantments() == null && item2.getEnchantments() == null) {
                                        itemsMatch = true;
                                    }
                                }
                                if (itemsMatch) {
                                    toRepair = item2;
                                }
                            }
                        }
                    }
                    if (itemsMatch) {
                        event.setCancelled(true);
                        event.getItem().remove();
                        toRepair.setDurability((short) (toRepair.getDurability() - (item1.getType().getMaxDurability() - item1.getDurability()) < 0 ? 0 : toRepair.getDurability() - (item1.getType().getMaxDurability() - item1.getDurability())));
                    }
                }
            }
        }
    }
}
