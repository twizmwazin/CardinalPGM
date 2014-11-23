package in.twizmwaz.cardinal.module.modules.kit.kit.contents;

import in.parapengu.commons.utils.StringUtils;
import org.bukkit.inventory.ItemStack;

/**
 * Created by kevin on 11/21/14.
 * <item slot="1" damage="-3000" enchantment="arrow infinite:1;arrow damage:2">bow</item>
 */
public class KitItem {

    private int slot;
    private ItemStack item;

    public KitItem(String item, int damage, String enchantments, int slot, int amount) {
        this.slot = slot;
        this.item = new ItemStack(StringUtils.convertStringToMaterial(item), amount, (short) damage);

        for (String a : enchantments.split(";")) {
            if (a.split(":").length < 2) {
                this.item.addEnchantment(StringUtils.convertStringToEnchantment(a), 1);
            } else {
                String[] b = a.split(":");
                this.item.addEnchantment(StringUtils.convertStringToEnchantment(b[0]), Integer.parseInt(b[1]));
            }
        }

    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItem() {
        return item;
    }
}
