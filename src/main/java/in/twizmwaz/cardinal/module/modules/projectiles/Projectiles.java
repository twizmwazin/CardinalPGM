package in.twizmwaz.cardinal.module.modules.projectiles;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Projectiles implements Module {

    private final EntityType projectile;
    private double velocityMod = 1.0;
    private List<PotionEffect> potionEffects = new ArrayList<>();

    protected Projectiles(final EntityType projectile, final double velocityMod, List<PotionEffect> potionEffects) {
        this.projectile = projectile;
        this.velocityMod = velocityMod;
        this.potionEffects = potionEffects;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        if (!projectile.equals(EntityType.ARROW) || velocityMod != 1.0) {
            Vector vector = event.getProjectile().getVelocity();
            event.setProjectile(GameHandler.getGameHandler().getMatchWorld().spawnEntity(event.getProjectile().getLocation(), projectile));
            ((Projectile) event.getProjectile()).setShooter(event.getEntity());
            event.getProjectile().setVelocity(vector.multiply(velocityMod));
            event.getProjectile().setMetadata("custom", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
        }
    }

    @EventHandler
    public void onEntityHitByProjectile(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getCause().equals(DamageCause.PROJECTILE)) {
            ProjectileSource source = ((Projectile) event.getDamager()).getShooter();
            if (source instanceof Player) {
                ((Player) source).playSound(((Player) source).getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.2F, 0.5F);
            }
            if (event.getDamager().getType().equals(projectile) && event.getDamager().hasMetadata("custom")) {
                Entity arrow = event.getEntity().getWorld().spawnEntity(event.getDamager().getLocation(), EntityType.ARROW);
                ((Projectile) arrow).setShooter(source);
                arrow.setVelocity(event.getDamager().getVelocity());
                event.getDamager().remove();
                if (event.getEntity() instanceof LivingEntity) {
                    for (PotionEffect effect : potionEffects) {
                        ((LivingEntity) event.getEntity()).addPotionEffect(effect);
                    }
                    final Entity entity = event.getEntity();
                    Bukkit.getServer().getScheduler().runTaskLater(GameHandler.getGameHandler().getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            ((LivingEntity) entity).setArrowsStuck(((LivingEntity) entity).getArrowsStuck() - 1);
                        }
                    }, 0);
                }
            }
        }
    }

}
