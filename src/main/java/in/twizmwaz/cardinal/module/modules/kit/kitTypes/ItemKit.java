package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.util.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class ItemKit implements Kit {

    List<KitItem> items;

    public ItemKit(List<KitItem> items) {
        this.items = items;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        PlayerInventory inventory = player.getInventory();

        for (KitItem item : items) {
            if (!item.hasSlot()) {
                inventory.addItem(item.getItem());
                continue;
            }
            if (force) {
                inventory.setItem(item.getSlot(), item.getItem());
            } else {
                // Repair tools
                if (item.getItem().getAmount() > 0 && item.getItem().getType().getMaxDurability() > 0) {
                    for (ItemStack item2 : inventory.getContents()) {
                        if (item2 == null) continue;
                        if (Items.toMaxDurability(item2).isSimilar(Items.toMaxDurability(item.getItem())) && item.getItem().getDurability() > item2.getDurability()) {
                            item2.setDurability(item.getItem().getDurability());
                            item.getItem().setAmount(0);
                            break;
                        }
                    }
                }
                // Stack items
                if (item.getItem().getAmount() > 0) {
                    for (ItemStack item2 : inventory.getContents()) {
                        if (item2 == null || item.getItem().getAmount() == 0) continue;
                        if (item2.isSimilar(item.getItem()) && item2.getAmount() != item2.getMaxStackSize()) {
                            int max = Math.min(item2.getMaxStackSize() - item2.getAmount(), item.getItem().getAmount());
                            item2.setAmount(item2.getAmount() + max);
                            item.getItem().setAmount(item.getItem().getAmount() - max);
                        }
                    }
                }
                // Put item in slot or give item
                if (item.getItem().getAmount() > 0) {
                    if (inventory.getItem(item.getSlot()) == null) {
                        inventory.setItem(item.getSlot(), item.getItem());
                    } else {
                        inventory.addItem(item.getItem());
                    }
                }
            }
        }

    }

}
