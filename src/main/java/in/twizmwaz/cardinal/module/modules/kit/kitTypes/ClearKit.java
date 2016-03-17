package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ClearKit implements Kit {

    Boolean clear;
    Boolean clearItems;

    public ClearKit(Boolean clear, Boolean clearItems) {
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
        if (clear) {
            for (ItemStack armor : player.getInventory().getArmorContents()) {
                armor.setAmount(0);
            }
        }
    }

}
