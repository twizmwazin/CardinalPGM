package in.twizmwaz.cardinal.module.modules.stats;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tracker {

    private final Player damaged;
    private final Player damager;
    private final ItemStack weapon;

    protected Tracker(Player damaged, Player damager, ItemStack weapon) {
        this.damaged = damaged;
        this.damager = damager;
        this.weapon = weapon;
    }

    public Player getDamaged() {
        return damaged;
    }

    public Player getDamager() {
        return damager;
    }

    public ItemStack getWeapon() {
        return weapon;
    }
}
