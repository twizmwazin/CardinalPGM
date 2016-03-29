package in.twizmwaz.cardinal.module.modules.itemMods;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ItemMods implements Module {

    Set<ItemRule> rules;

    public ItemMods(Set<ItemRule> rules) {
        this.rules = rules;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public ItemStack applyRules(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return itemStack;
        for (ItemRule rule : rules) {
            rule.apply(itemStack);
        }
        return itemStack;
    }

    private void applyRules(Inventory inventory) {
        for (ItemStack itemStack : inventory.getContents()) {
            applyRules(itemStack);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        applyRules(event.getInventory());
    }

    @EventHandler
    public void onItemPickupEvent(PlayerPickupItemEvent event) {
        applyRules(event.getItem().getItemStack());
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        applyRules(event.getCurrentItem());
    }

    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent event) {
        applyRules(event.getInventory().getResult());
    }

}
