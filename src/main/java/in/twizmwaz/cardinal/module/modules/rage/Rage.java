package in.twizmwaz.cardinal.module.modules.rage;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

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
                if (item.containsEnchantment(Enchantment.DAMAGE_ALL) && item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 1) {
                    event.setDamage(1000);
                }
            }
        } else if (event.getDamager() instanceof Arrow) {
            if (event.getDamager().hasMetadata("rage")) {
                event.setDamage(1000);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityBowShoot(EntityShootBowEvent event) {
        if (event.getBow() != null && event.getBow().containsEnchantment(Enchantment.ARROW_DAMAGE) && event.getBow().getEnchantmentLevel(Enchantment.ARROW_DAMAGE) > 1) {
            event.getProjectile().setMetadata("rage", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
        }
    }
}
