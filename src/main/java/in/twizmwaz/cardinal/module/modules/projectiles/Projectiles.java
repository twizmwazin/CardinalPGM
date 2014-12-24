package in.twizmwaz.cardinal.module.modules.projectiles;

import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Projectiles implements Module {

    private EntityType projectile;
    private double velocityMod = 1.0;
    private List<PotionEffect> potionEffects = new ArrayList<>();

    protected Projectiles(EntityType projectile, double velocityMod, List<PotionEffect> potionEffects) {
        this.projectile = projectile;
        this.velocityMod = velocityMod;
        this.potionEffects = potionEffects;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getType() == EntityType.ARROW) {
            Vector velocity = event.getEntity().getVelocity();
            velocity = velocity.multiply(velocityMod);
            Location location = event.getEntity().getLocation();
            location = location.add(velocity);
            World world = event.getEntity().getWorld();
            event.setCancelled(true);
            Entity spawned = world.spawnEntity(location, projectile);
            if (spawned.getType().equals(EntityType.PRIMED_TNT)) {
                ((TNTPrimed) spawned).setFuseTicks(80);
            }
            spawned.setVelocity(velocity);
            spawned.setMetadata("source", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), event.getEntity().getShooter() != null ? (event.getEntity().getShooter() instanceof Player ? ((Player) event.getEntity().getShooter()).getName() : "null") : "null"));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getCause().equals(DamageCause.PROJECTILE) && event.getDamager().getType().equals(projectile) && event.getEntity() instanceof Player) {
            if (event.getDamager().hasMetadata("source")) {
                if (event.getDamager().getMetadata("source").get(0).asString().equals(((Player) event.getEntity()).getName())) {
                    Vector velocity = event.getDamager().getVelocity();
                    Location location = event.getDamager().getLocation();
                    location = location.add(velocity);
                    World world = event.getDamager().getWorld();
                    int fuse = 80;
                    if (event.getDamager().getType().equals(EntityType.PRIMED_TNT)) {
                        fuse = ((TNTPrimed) event.getDamager()).getFuseTicks();
                    }
                    event.setCancelled(true);
                    Entity spawned = world.spawnEntity(location, projectile);
                    if (spawned.getType().equals(EntityType.PRIMED_TNT)) {
                        ((TNTPrimed) spawned).setFuseTicks(fuse);
                    }
                    spawned.setVelocity(velocity);
                    spawned.setMetadata("source", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), ((Player) event.getEntity()).getName()));
                }
            }
        }
        if (!event.isCancelled()) {
            if (event.getCause() == DamageCause.PROJECTILE && event.getEntity() instanceof LivingEntity) {
                for (PotionEffect potionEffect : this.potionEffects) {
                    ((LivingEntity) event.getEntity()).addPotionEffect(potionEffect);
                }
            }
        }
    }
}
