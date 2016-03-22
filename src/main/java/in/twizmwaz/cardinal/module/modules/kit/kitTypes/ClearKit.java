package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ClearKit implements Kit {

    boolean clear;
    boolean clearItems;

    public ClearKit(boolean clear, boolean clearItems) {
        this.clear = clear;
        this.clearItems = clearItems;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        if (clear || clearItems) player.getInventory().clear();
        if (clear) player.getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)});
    }

}
