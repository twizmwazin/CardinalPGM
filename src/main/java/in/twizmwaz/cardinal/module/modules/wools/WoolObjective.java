package in.twizmwaz.cardinal.module.modules.wools;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.event.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.material.Wool;

public class WoolObjective implements GameObjective {

    private final PgmTeam team;
    private final String name;
    private final String id;
    private final DyeColor color;
    private final BlockRegion place;

    private boolean touched;
    private boolean complete;

    protected WoolObjective(final PgmTeam team, final String name, final String id, final DyeColor color, final BlockRegion place) {
        this.team = team;
        this.name  = name;
        this.id = id;
        this.color = color;
        this.place = place;
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
    public void onPickup(PlayerPickupItemEvent event) {
        this.touched = true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            if (event.getBlock().getType().equals(Material.WOOL)) {
                    if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    this.complete = true;
                    Bukkit.broadcastMessage(team.getColor() + event.getPlayer().getDisplayName() + ChatColor.WHITE + " placed " + StringUtils.convertDyeColorToChatColor(color) + getName().toUpperCase() + ChatColor.WHITE + " for the " + team.getColor() + team.getName());
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this);
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
                    event.setCancelled(false);
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

}
