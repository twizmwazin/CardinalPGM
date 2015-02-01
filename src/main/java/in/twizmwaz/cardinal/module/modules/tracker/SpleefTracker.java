package in.twizmwaz.cardinal.module.modules.tracker;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.module.modules.tracker.event.PlayerSpleefEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;
import java.util.UUID;

public class SpleefTracker implements Module {

    private static HashMap<UUID, PlayerSpleefEvent> playerSpleefEvents = new HashMap<>();

    protected SpleefTracker() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public enum Type {
        PLAYER(), TNT()
    }

    public static PlayerSpleefEvent getLastSpleefEvent(Player player) {
        return playerSpleefEvents.containsKey(player.getUniqueId()) ? playerSpleefEvents.get(player.getUniqueId()) : null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = event.getBlock().getLocation();
            location.setY(location.getY() + 1);
            Location playerLoc = player.getLocation();
            if (playerLoc.getBlockX() == location.getBlockX() && playerLoc.getBlockY() == location.getBlockY() && playerLoc.getBlockZ() == location.getBlockZ()) {
                Bukkit.getServer().getPluginManager().callEvent(new PlayerSpleefEvent(player, event.getPlayer(), event.getPlayer().getItemInHand(), Type.PLAYER));
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
                    OfflinePlayer damager = (TntTracker.getWhoPlaced(event.getEntity()) != null ? Bukkit.getOfflinePlayer(TntTracker.getWhoPlaced(event.getEntity())) : null);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerSpleefEvent(player, damager, (damager != null && damager.isOnline() ? ((Player) damager).getItemInHand() : null), Type.TNT));
                }
            }
        }
    }

    @EventHandler
    public void onSpleefEvent(PlayerSpleefEvent event) {
        playerSpleefEvents.put(event.getSpleefed().getUniqueId(), event);
    }

}
