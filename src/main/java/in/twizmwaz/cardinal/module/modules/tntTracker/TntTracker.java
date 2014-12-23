package in.twizmwaz.cardinal.module.modules.tntTracker;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class TntTracker implements Module {
    private HashMap<String, String> tntPlaced = new HashMap<>();

    protected TntTracker() { }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.TNT) {
            Location location = event.getBlock().getLocation();
            tntPlaced.put(location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ(), event.getPlayer().getName());
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getEntity().getType() == EntityType.PRIMED_TNT) {
            Location location = event.getEntity().getLocation();
            if (tntPlaced.containsKey(location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ())) {
                String player = tntPlaced.get(location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ());
                event.getEntity().setMetadata("source", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), player));
                tntPlaced.remove(location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ());
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().getType() == EntityType.PRIMED_TNT) {
            for (Block block : event.blockList()) {
                if (block.getType() == Material.TNT && getWhoPlaced(event.getEntity()) != null) {
                    Location location = block.getLocation();
                    tntPlaced.put(location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ(), getWhoPlaced(event.getEntity()));
                }
            }
        }
    }

    public static String getWhoPlaced(Entity tnt) {
        if (tnt.getType() == EntityType.PRIMED_TNT) {
            if (tnt.hasMetadata("source")) {
                return tnt.getMetadata("source").get(0).asString();
            }
        }
        return null;
    }
}
