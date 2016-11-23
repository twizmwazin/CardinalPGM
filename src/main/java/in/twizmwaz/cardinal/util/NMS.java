package in.twizmwaz.cardinal.util;

import net.minecraft.server.NBTTagCompound;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMS {

    public static final String TAG = "Cardinal";

    public static final String APPLY_ITEM_NBT = "item-mod-applied";

    public static NBTTagCompound getItemNBT(ItemStack item) {
        return item instanceof CraftItemStack ? ((CraftItemStack) item).getHandle().c(TAG) : new NBTTagCompound();
    }

}
