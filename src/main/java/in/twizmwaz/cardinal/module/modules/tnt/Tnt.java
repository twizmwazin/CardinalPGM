package in.twizmwaz.cardinal.module.modules.tnt;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Tnt implements Module {

    private final boolean instantIgnite;
    private final boolean blockDamage;
    private final double yield;
    private final double power;
    private final int fuse;

    protected Tnt(final boolean instantIgnite, final boolean blockDamage, final double yield, final double power, final int fuse) {
        this.instantIgnite = instantIgnite;
        this.blockDamage = blockDamage;
        this.yield = yield;
        this.power = power;
        this.fuse = fuse;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (block.getType().equals(Material.TNT) && instantIgnite && !event.isCancelled()) {
            event.getBlock().setType(Material.AIR);
            TNTPrimed tnt = (TNTPrimed) GameHandler.getGameHandler().getMatchWorld().spawnEntity(block.getLocation().add(new Vector(0.5, 0.5, 0.5)), EntityType.PRIMED_TNT);
            Bukkit.getServer().getPluginManager().callEvent(new ExplosionPrimeEvent(tnt, (float) power * 10, false));
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            if (!blockDamage) {
                event.blockList().clear();
            } else if (yield != 0.3){
                event.setYield((float)yield);
            }
        }
    }


}
