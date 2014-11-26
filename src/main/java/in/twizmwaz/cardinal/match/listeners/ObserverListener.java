package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupExperienceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by kevin on 11/25/14.
 */
public class ObserverListener implements Listener {

    private final JavaPlugin plugin;
    private final Match match;

    public ObserverListener(JavaPlugin plugin, Match match) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        this.match = match;
    }

    @EventHandler
    public void onBlockChange(BlockPlaceEvent event) {
        if (match.getTeamById("observers").hasPlayer(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockChange(BlockBreakEvent event) {
        if (match.getTeamById("observers").hasPlayer(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if (match.getTeamById("observers").hasPlayer(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
            if (event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                    Inventory chest = Bukkit.createInventory(null, ((Chest) event.getClickedBlock().getState()).getInventory().getSize(), (((Chest) event.getClickedBlock().getState()).getInventory().getSize() == 54 ? "Large Chest" : "Chest"));
                    for (int i = 0; i < ((Chest) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        chest.setItem(i, ((Chest) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(chest);
                }
                if (event.getClickedBlock().getType().equals(Material.FURNACE)) {
                    Inventory furnace = Bukkit.createInventory(null, InventoryType.FURNACE, "Furnace");
                    for (int i = 0; i < ((Furnace) event.getClickedBlock().getState()).getInventory().getSize(); i++) {
                        furnace.setItem(i, ((Furnace) event.getClickedBlock().getState()).getInventory().getItem(i));
                    }
                    event.getPlayer().openInventory(furnace);
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (match.getTeamById("observers").hasPlayer((Player)event.getWhoClicked()) || match.getState() != MatchState.PLAYING) {
            if (event.getInventory().getType() != InventoryType.PLAYER){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (match.getTeamById("observers").hasPlayer(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickupXP(PlayerPickupExperienceEvent event) {
        if (match.getTeamById("observers").hasPlayer(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (match.getTeamById("observers").hasPlayer(event.getPlayer()) || match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

}
