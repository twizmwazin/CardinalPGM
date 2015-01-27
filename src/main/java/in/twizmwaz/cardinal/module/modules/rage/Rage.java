package in.twizmwaz.cardinal.module.modules.rage;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Rage implements Module{

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            ItemStack item = ((Player) event.getDamager()).getItemInHand();
            if (item != null) {
                if (item.containsEnchantment(Enchantment.DAMAGE_ALL) || item.getType().name().contains("SWORD")) {
                    event.setDamage(1000);
                }
            }
        } else if (event.getDamager() instanceof Arrow) {
            if (((Arrow) event.getDamager()).getShooter() instanceof Player) {
                Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();
                for (ItemStack item : shooter.getInventory()) {
                    if (item != null) {
                        if (item.getType().equals(Material.BOW) && item.containsEnchantment(Enchantment.ARROW_DAMAGE)) {
                            event.setDamage(1000);
                        }
                    }
                }
            }
        }
    }
}
