package in.twizmwaz.cardinal.module.modules.tnt;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class Tnt implements Module {

    private final boolean instantIgnite;
    private final boolean blockDamage;
    private final double yield;
    private final double power;
    private final int fuse;
    private final int limit;
    private final double multiplier;

    protected Tnt(final boolean instantIgnite, final boolean blockDamage, final double yield, final double power, final double fuse, final int limit, final double multiplier) {
        this.instantIgnite = instantIgnite;
        this.blockDamage = blockDamage;
        this.yield = yield;
        this.power = power;
        this.fuse = (int) (fuse * 20);
        this.limit = limit;
        this.multiplier = multiplier;
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
            Bukkit.getServer().getPluginManager().callEvent(new ExplosionPrimeEvent(tnt));
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getEntity();
            if (fuse != 4) {
                tnt.setFuseTicks(fuse);
            }
            if (power != 4.0) {
                tnt.setYield((float) power);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && event.getEntity() instanceof TNTPrimed) {
            if (!blockDamage) {
                event.blockList().clear();
            } else if (yield != 0.3){
                event.setYield((float)yield);
            }
            UUID player = TntTracker.getWhoPlaced(event.getEntity());
            for (Block block : event.blockList()) {
                if (block.getState() instanceof Dispenser) {
                    Inventory inventory = ((Dispenser) block.getState()).getInventory();
                    Location location = block.getLocation();
                    double tntCount = 0;
                    for (ItemStack itemstack : inventory.getContents()) {
                        if (itemstack != null && itemstack.getType() == Material.TNT) tntCount += itemstack.getAmount() * multiplier;
                        if (tntCount >= limit) {
                            tntCount = limit;
                            break;
                        }
                    }
                    inventory.remove(Material.TNT);
                    if (tntCount > 0) {
                        Random random = new Random();
                        for (double i = tntCount; i > 0; i--) {
                            TNTPrimed tnt = event.getWorld().spawn(location, TNTPrimed.class);
                            Vector velocity = new Vector((1.5 * random.nextDouble()) - 0.75, (1.5 * random.nextDouble()) - 0.75, (1.5 * random.nextDouble()) - 0.75);
                            tnt.setVelocity(velocity);
                            tnt.setFuseTicks(random.nextInt(10) + 10);
                            if (player != null) {
                                tnt.setMetadata("source", new FixedMetadataValue(Cardinal.getInstance(), player));
                            }
                        }
                    }
                }
            }
        }
    }

}
