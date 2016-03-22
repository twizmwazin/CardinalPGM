package in.twizmwaz.cardinal.module.modules.kit.kitTypes;

import in.twizmwaz.cardinal.module.modules.kit.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.List;

public class ArmorKit implements Kit {

    List<KitArmor> armor;

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
            switch (armor.getType()) {
                case HELMET:
                    player.getInventory().setHelmet(armor.getItem());
                    break;
                case CHESTPLATE:
                    player.getInventory().setChestplate(armor.getItem());
                    break;
                case LEGGINGS:
                    player.getInventory().setLeggings(armor.getItem());
                    break;
                case BOOTS:
                    player.getInventory().setBoots(armor.getItem());
                    break;
            }
        }
    }

}
