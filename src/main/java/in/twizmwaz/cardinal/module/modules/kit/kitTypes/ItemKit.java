package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.util.Items;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class ItemKit implements Kit {

    private List<KitItem> items;

    public ItemKit(List<KitItem> items) {
        this.items = items;
    }

    public static List<KitItem> cloneItems(List<KitItem> items) {
        List<KitItem> clone = new ArrayList<>(items.size());
        for(KitItem item : items) clone.add(item.getCopy());
        return clone;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        PlayerInventory inventory = player.getInventory();
        List<KitItem> items = cloneItems(this.items);

        // Remove kit items that the player already has
        for(ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            ItemStack itemClone = item.clone();
            for(KitItem item2 : items) {
                if(Items.itemsEqual(item2.getItem(), item) && !(item2.getItem().getDurability() < item.getDurability())) {
                    int remove = itemClone.getAmount();
                    if (item2.getItem().getAmount() < remove) remove = item2.getItem().getAmount();
                    itemClone.setAmount(itemClone.getAmount() - remove);
                    item2.getItem().setAmount(item2.getItem().getAmount() - remove);
                }
            }
        }

        for (KitItem item : items) {
            ItemStack kitItem = item.getItem();
            if (!item.hasSlot()) {
                inventory.addItem(kitItem);
                continue;
            }
            if (force) {
                setPlayerItem(player, item.getSlot(), kitItem);
            } else {
                // Repair tools
                if (kitItem.getAmount() > 0 && kitItem.getType().getMaxDurability() > 0) {
                    for (ItemStack item2 : inventory.getContents()) {
                        if (item2 == null) continue;
                        if (Items.itemsEqual(kitItem, item2) && kitItem.getDurability() < item2.getDurability()) {
                            item2.setDurability(kitItem.getDurability());
                            kitItem.setAmount(0);
                            break;
                        }
                    }
                }
                // Stack items
                if (kitItem.getAmount() > 0) {
                    for (ItemStack item2 : inventory.getContents()) {
                        if (item2 == null || kitItem.getAmount() == 0) continue;
                        if (Items.itemsEqual(kitItem, item2) && item2.getAmount() < item2.getMaxStackSize()) {
                            int max = Math.min(item2.getMaxStackSize() - item2.getAmount(), kitItem.getAmount());
                            item2.setAmount(item2.getAmount() + max);
                            kitItem.setAmount(kitItem.getAmount() - max);
                        }
                    }
                }
                // Put item in slot or give item
                if (kitItem.getAmount() > 0) {
                    if (inventory.getItem(item.getSlot()) == null) {
                        setPlayerItem(player, item.getSlot(), kitItem);
                    } else {
                        inventory.addItem(kitItem);
                    }
                }
            }
        }

    }


    // This uses the NMS method that /replacitem uses, allows for the use of
    // slot.weapon.offhand, and such
    public static void setPlayerItem(Player player, int slot, ItemStack item) {
        ((CraftPlayer)player).getHandle().c(slot, CraftItemStack.asNMSCopy(item));
    }

}
