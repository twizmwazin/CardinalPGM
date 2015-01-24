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
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damagedPlayer = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            ItemStack itemInHand = damager.getInventory().getItemInHand();
            if (itemInHand.containsEnchantment(Enchantment.DAMAGE_ALL) && itemInHand.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 1) {
                event.setDamage(100);
            }

        } else if (event.getDamager() instanceof Arrow && event.getEntity() instanceof Player){
            if (((Arrow) event.getDamager()).getShooter() instanceof Player){
                Player shooter = (Player) ((Arrow) event.getDamager()).getShooter();
                Inventory inv = shooter.getInventory();
                for (ItemStack item : inv){
                    if (item.getType().equals(Material.BOW) && item.containsEnchantment(Enchantment.ARROW_DAMAGE) && item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) >= 1){
                        event.setDamage(100);
                    }
                }
            }
        }
    }
}
