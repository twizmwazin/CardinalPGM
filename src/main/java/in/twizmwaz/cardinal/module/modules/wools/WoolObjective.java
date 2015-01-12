package in.twizmwaz.cardinal.module.modules.wools;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import java.util.HashSet;
import java.util.Set;

public class WoolObjective implements GameObjective {

    private final PgmTeam team;
    private final String name;
    private final String id;
    private final DyeColor color;
    private final BlockRegion place;
    private final boolean craftable;

    private Set<String> playersTouched;
    private boolean touched;
    private boolean complete;

    protected WoolObjective(final PgmTeam team, final String name, final String id, final DyeColor color, final BlockRegion place, final boolean craftable) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.color = color;
        this.place = place;
        this.craftable = craftable;

        this.playersTouched = new HashSet<>();
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public PgmTeam getTeam() {
        return team;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isTouched() {
        return touched;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @EventHandler
    public void onWoolPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!this.complete) {
            try {
                if (event.getItem().getItemStack().getType() == Material.WOOL && event.getItem().getItemStack().getData().getData() == color.getData()) {
                    if (GameHandler.getGameHandler().getMatch().getTeam(player).equals(team)) {
                        if (!this.playersTouched.contains(player.getName())) {
                            this.playersTouched.add(player.getName());
                        }
                        this.touched = true;
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getName())) {
            playersTouched.remove(event.getEntity().getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            if (event.getBlock().getType().equals(Material.WOOL)) {
                if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    if (GameHandler.getGameHandler().getMatch().getTeam(event.getPlayer()).equals(team)) {
                        this.complete = true;
                        Bukkit.broadcastMessage(team.getColor() + event.getPlayer().getDisplayName() + ChatColor.WHITE + " placed " + StringUtils.convertDyeColorToChatColor(color) + getName().toUpperCase() + ChatColor.WHITE + " for the " + team.getColor() + team.getName());
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this);
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                        event.setCancelled(false);
                    } else event.setCancelled(true);
                } else event.setCancelled(true);
            } else event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getLocation().equals(place.getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraftWool(CraftItemEvent event) {
        if (event.getRecipe().getResult().equals(new ItemStack(Material.WOOL, 1, color.getData())) && this.craftable) {
            event.setCancelled(true);
        }
    }
}
