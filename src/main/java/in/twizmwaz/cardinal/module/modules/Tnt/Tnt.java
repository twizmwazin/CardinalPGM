package in.twizmwaz.cardinal.module.modules.Tnt;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.logging.Level;

public class Tnt implements Module {

    private final boolean instantIgnite;
    private final boolean blockDamage;
    private final double yield;
    private final double power;
    private final int fuse;

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    protected Tnt(final boolean instantIgnite, final boolean blockDamage, final double yield, final double power, final int fuse) {
        this.instantIgnite = instantIgnite;
        this.blockDamage = blockDamage;
        this.yield = yield;
        this.power = power;
        this.fuse = fuse;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().equals(Material.TNT) && instantIgnite) {
            event.setCancelled(true);
            Entity tntPrimed = GameHandler.getGameHandler().getMatchWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            tntPrimed.setMetadata("instantignite", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            if (fuse != 4) {
                ((TNTPrimed) event.getEntity()).setFuseTicks(fuse * 20);
            }
            if (power != 4.0) {
                event.setRadius((float) power * 10);
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            if (blockDamage) {
                if (event.getEntity().hasMetadata("instantignite")) {
                    if (instantIgnite && event.getEntity().getMetadata("instantignite").get(0).value().equals(true)) {
                        event.setCancelled(true);
                        GameHandler.getGameHandler().getMatchWorld().createExplosion(event.getLocation(), (float) power);
                    }
                }
                if (yield != 0.3) {
                    /**
                     * Currently Broken
                     *
                     * TODO: Fix it.
                     */

                    event.setYield((float) yield);


                }
            } else {
                event.setCancelled(true);
                GameHandler.getGameHandler().getMatchWorld().createExplosion(event.getLocation().getX(), event.getLocation().getY(), event.getLocation().getZ(), (float) power, false, false);
            }
        }
    }


}
