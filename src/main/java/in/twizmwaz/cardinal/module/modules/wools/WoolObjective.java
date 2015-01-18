package in.twizmwaz.cardinal.module.modules.wools;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.chat.GlobalChat;
import in.twizmwaz.cardinal.chat.TeamChat;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WoolObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final DyeColor color;
    private final BlockRegion place;
    private final boolean craftable;
    private final boolean show;

    private Set<UUID> playersTouched;
    private boolean touched;
    private boolean complete;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected WoolObjective(final TeamModule team, final String name, final String id, final DyeColor color, final BlockRegion place, final boolean craftable, final boolean show) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.color = color;
        this.place = place;
        this.craftable = craftable;
        this.show = show;

        this.playersTouched = new HashSet<>();

        this.scoreboardHandler = new GameObjectiveScoreboardHandler(this);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public TeamModule getTeam() {
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

    @Override
    public boolean showOnScoreboard() {
        return show;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @EventHandler
    public void onWoolPickup(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.complete) {
            try {
                if (event.getCurrentItem().getType() == Material.WOOL && event.getCurrentItem().getData().getData() == color.getData()) {
                    if (TeamUtils.getTeamByPlayer(player) == team) {
                        if (!this.playersTouched.contains(player.getUniqueId())) {
                            this.playersTouched.add(player.getUniqueId());
                            TeamChat.sendToTeam(team.getColor() + "[Team] " + player.getDisplayName() + ChatColor.GRAY + " picked up " + StringUtils.convertDyeColorToChatColor(color) + getName().toUpperCase(), team);
                        }
                        boolean oldState = this.touched;
                        this.touched = true;
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, !oldState);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @EventHandler
    public void onWoolPickup(PlayerPickupItemEvent event) {
        Player player = (Player) event.getPlayer();
        if (!this.complete) {
            try {
                if (event.getItem().getItemStack().getType() == Material.WOOL && event.getItem().getItemStack().getData().getData() == color.getData()) {
                    if (TeamUtils.getTeamByPlayer(player) == team) {
                        if (!this.playersTouched.contains(player.getUniqueId())) {
                            this.playersTouched.add(player.getUniqueId());
                            TeamChat.sendToTeam(team.getColor() + "[Team] " + player.getDisplayName() + ChatColor.GRAY + " picked up " + StringUtils.convertDyeColorToChatColor(color) + getName().toUpperCase(), team);
                        }
                        boolean oldState = this.touched;
                        this.touched = true;
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, !oldState);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getUniqueId())) {
            playersTouched.remove(event.getEntity().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            if (event.getBlock().getType().equals(Material.WOOL)) {
                if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    if (TeamUtils.getTeamByPlayer(event.getPlayer()) == team) {
                        this.complete = true;
                        Bukkit.broadcastMessage(team.getColor() + event.getPlayer().getDisplayName() + ChatColor.WHITE + " placed " + StringUtils.convertDyeColorToChatColor(color) + getName().toUpperCase() + ChatColor.WHITE + " for the " + team.getCompleteName());
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        ChatUtils.sendWarningMessage(event.getPlayer(), "You may not complete the other team's objective.");
                    }
                } else {
                    event.setCancelled(true);
                    ChatUtils.sendWarningMessage(event.getPlayer(), "Only " + StringUtils.convertDyeColorToChatColor(color) + color.name().replaceAll("_", " ").toUpperCase() + " WOOL" + ChatColor.RED + " may be placed here!");
                }
            } else {
                event.setCancelled(true);
                ChatUtils.sendWarningMessage(event.getPlayer(), "Only " + StringUtils.convertDyeColorToChatColor(color) + color.name().replaceAll("_", " ").toUpperCase() + " WOOL" + ChatColor.RED + " may be placed here!");
            }
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
