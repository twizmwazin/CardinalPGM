package in.twizmwaz.cardinal.module.modules.tracker;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class DamageTracker implements Module {

    private static HashMap<UUID, TrackerDamageEvent> trackerDamageEvents = new HashMap<>();

    protected DamageTracker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public enum Type {
        SHOT(), KNOCKED(), BLOWN()
    }

    public enum SpecificType {
        OUT_OF_THE_WATER(), OUT_OF_THE_LAVA(), OFF_A_LADDER(), OFF_A_VINE()
    }

    public static TrackerDamageEvent getLastDamageEvent(Player player) {
        return trackerDamageEvents.containsKey(player.getUniqueId()) ? trackerDamageEvents.get(player.getUniqueId()) : null;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            TrackerDamageEvent damageEvent = null;
            if (event.getCause().equals(DamageCause.PROJECTILE)) {
                if (((Projectile) event.getDamager()).getShooter() instanceof OfflinePlayer) {
                    OfflinePlayer damager = (OfflinePlayer) ((Projectile) event.getDamager()).getShooter();
                    damageEvent = new TrackerDamageEvent((Player) event.getEntity(), damager, (damager.isOnline() ? ((Player) damager).getItemInHand() : null), Type.SHOT);
                }
            } else if (event.getCause().equals(DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
                if (TntTracker.getWhoPlaced(event.getDamager()) != null) {
                    OfflinePlayer damager = Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(event.getDamager()));
                    damageEvent = new TrackerDamageEvent((Player) event.getEntity(), damager, (damager.isOnline() ? ((Player) damager).getItemInHand() : null), Type.BLOWN);
                }
            } else {
                if (event.getDamager() instanceof OfflinePlayer) {
                    OfflinePlayer damager = (OfflinePlayer) event.getDamager();
                    damageEvent = new TrackerDamageEvent((Player) event.getEntity(), damager, (damager.isOnline() ? ((Player) damager).getItemInHand() : null), Type.KNOCKED);
                }
            }
            if (damageEvent == null) return;
            if (event.getEntity().getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.LADDER)) {
                damageEvent.setSpecificType(SpecificType.OFF_A_LADDER);
            } else if (event.getEntity().getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.VINE)) {
                damageEvent.setSpecificType(SpecificType.OFF_A_VINE);
            } else if (event.getEntity().getLocation().getBlock().getType().equals(Material.WATER) || event.getEntity().getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
                damageEvent.setSpecificType(SpecificType.OUT_OF_THE_WATER);
            } else if (event.getEntity().getLocation().getBlock().getType().equals(Material.LAVA) || event.getEntity().getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)) {
                damageEvent.setSpecificType(SpecificType.OUT_OF_THE_LAVA);
            }
            Bukkit.getServer().getPluginManager().callEvent(damageEvent);
        }
    }

    @EventHandler
    public void onTrackerDamage(TrackerDamageEvent event) {
        trackerDamageEvents.put(event.getPlayer().getUniqueId(), event);
    }

}
