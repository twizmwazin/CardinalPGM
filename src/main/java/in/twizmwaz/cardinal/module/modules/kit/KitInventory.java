package in.twizmwaz.cardinal.module.modules.kit;

import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * Created by kevin on 11/28/14.
 */
public class KitInventory {

    private final String name;
    private final PlayerInventory inventory;
    private final List<PotionEffect> potionEffects;

    protected KitInventory(String name, PlayerInventory inventory, List<PotionEffect> potionEffects) {
        this.name = name;
        this.inventory = inventory;
        this.potionEffects = potionEffects;
    }

}
