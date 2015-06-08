package in.twizmwaz.cardinal.module.modules.tracker;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.module.modules.tracker.event.TrackerDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class SpleefTracker implements Module {

    private static HashMap<UUID, TrackerDamageEvent> events = new HashMap<>();

    protected SpleefTracker() {
    }

    public static TrackerDamageEvent getLastSpleefEvent(Player player) {
        return events.containsKey(player.getUniqueId()) ? events.get(player.getUniqueId()) : null;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = event.getBlock().getLocation();
            location.setY(location.getY() + 1);
            Location playerLoc = player.getLocation();
            if (playerLoc.getBlockX() == location.getBlockX() && playerLoc.getBlockY() == location.getBlockY() && playerLoc.getBlockZ() == location.getBlockZ()) {
                Description description = null;
                if (player.getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.LADDER)) {
                    description = Description.OFF_A_LADDER;
                } else if (player.getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.VINE)) {
                    description = Description.OFF_A_VINE;
                } else if (player.getLocation().getBlock().getType().equals(Material.WATER) || player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
                    description = Description.OUT_OF_THE_WATER;
                } else if (player.getLocation().getBlock().getType().equals(Material.LAVA) || player.getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)) {
                    description = Description.OUT_OF_THE_LAVA;
                }
                Bukkit.getServer().getPluginManager().callEvent(new TrackerDamageEvent(player, event.getPlayer(), event.getPlayer().getItemInHand(), Cause.PLAYER, description, Type.SPLEEFED));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location location = block.getLocation();
                location.setY(location.getY() + 1);
                Location playerLoc = player.getLocation();
                if (playerLoc.getBlockX() == location.getBlockX() && playerLoc.getBlockY() == location.getBlockY() && playerLoc.getBlockZ() == location.getBlockZ()) {
                    Description description = null;
                    if (player.getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.LADDER)) {
                        description = Description.OFF_A_LADDER;
                    } else if (player.getLocation().add(new Vector(0, 1, 0)).getBlock().getType().equals(Material.VINE)) {
                        description = Description.OFF_A_VINE;
                    } else if (player.getLocation().getBlock().getType().equals(Material.WATER) || player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)) {
                        description = Description.OUT_OF_THE_WATER;
                    } else if (player.getLocation().getBlock().getType().equals(Material.LAVA) || player.getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)) {
                        description = Description.OUT_OF_THE_LAVA;
                    }
                    OfflinePlayer damager = (TntTracker.getWhoPlaced(event.getEntity()) != null ? Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(event.getEntity())) : null);
                    Bukkit.getServer().getPluginManager().callEvent(new TrackerDamageEvent(player, damager, damager != null && damager.getPlayer() != null ? ((Player) damager).getItemInHand() : new ItemStack(Material.AIR), Cause.TNT, description, Type.SPLEEFED));
                }
            }
        }
    }

}
