package in.twizmwaz.cardinal.module.modules.kit;

import org.bukkit.inventory.ItemStack;

class KitItem {

    private final int slot;
    private final ItemStack item;

    protected KitItem(int slot, ItemStack item) {
        this.slot = slot;
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
