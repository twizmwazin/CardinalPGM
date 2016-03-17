package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import org.bukkit.inventory.ItemStack;

public class KitItem {

    private final int slot;
    private final ItemStack item;

    public KitItem(ItemStack item, int slot) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public boolean hasSlot() {
        return slot != -1;
    }

    public ItemStack getItem() {
        return item;
    }
}
