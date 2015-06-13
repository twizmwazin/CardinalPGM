package in.twizmwaz.cardinal.module.modules.kit;

import org.bukkit.inventory.ItemStack;

class KitItem {

    private final int slot;
    private final ItemStack item;

    protected KitItem(ItemStack item, int slot) {
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
