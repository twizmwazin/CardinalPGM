package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.util.ArmorType;
import org.bukkit.inventory.ItemStack;

public class KitArmor {

    private ItemStack item;
    private ArmorType type;
    private Boolean locked;

    public KitArmor(ItemStack item, ArmorType type, Boolean locked) {
        this.item = item;
        this.type = type;
        this.locked = locked;
    }

    public ItemStack getItem() {
        return item;
    }

    public ArmorType getType() {
        return type;
    }

    public Boolean isLocked() {
        return locked;
    }

}
