package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.List;

public class ArmorKit implements Kit {

    private List<KitArmor> armor;

    public ArmorKit(List<KitArmor> items) {
        this.armor = items;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void apply(Player player, Boolean force) {
        for (KitArmor armor : this.armor) {
            player.getInventory().setItem(armor.getType().getSlot(), armor.getItem());
        }
    }

}
