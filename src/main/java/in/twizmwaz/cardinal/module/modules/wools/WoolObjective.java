package in.twizmwaz.cardinal.module.modules.wools;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.snowflakes.Snowflakes;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.FireworkUtil;
import in.twizmwaz.cardinal.util.MiscUtils;
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
import org.bukkit.util.Vector;

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

    private Vector location;
    private double proximity;

    private Set<UUID> playersTouched;
    private boolean touched;
    private boolean complete;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected WoolObjective(final TeamModule team, final String name, final String id, final DyeColor color, final BlockRegion place, final boolean craftable, final boolean show, final Vector location) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.color = color;
        this.place = place;
        this.craftable = craftable;
        this.show = show;
        this.location = location;

        this.proximity = Double.POSITIVE_INFINITY;

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
        if (!this.complete && GameHandler.getGameHandler().getMatch().isRunning()) {
            try {
                if (event.getCurrentItem().getType() == Material.WOOL && event.getCurrentItem().getData().getData() == color.getData()) {
                    if (TeamUtils.getTeamByPlayer(player) == team) {
                        boolean touchMessage = false;
                        if (!this.playersTouched.contains(player.getUniqueId())) {
                            this.playersTouched.add(player.getUniqueId());
                            if (this.show && !this.complete) {
                                TeamUtils.getTeamChannel(team).sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY)));
                                for (Player player1 : Bukkit.getOnlinePlayers()) {
                                    if (TeamUtils.getTeamByPlayer(player1) != null && TeamUtils.getTeamByPlayer(player1).isObserver()) {
                                        player1.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY, team.getCompleteName() + ChatColor.GRAY)).getMessage(player1.getLocale()));
                                    }
                                }
                                touchMessage = true;
                            }
                        }
                        boolean oldState = this.touched;
                        this.touched = true;
                        if (touchMessage) {
                            double newProx;
                            if (location != null) {
                                newProx = location.distance(place.getVector());
                            } else {
                                newProx = player.getLocation().toVector().distance(place.getVector());
                            }
                            if (!oldState || newProx < proximity) {
                                proximity = newProx;
                            }
                        }
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, !oldState, touchMessage);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
    }

    @EventHandler
    public void onWoolPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!this.complete && GameHandler.getGameHandler().getMatch().isRunning()) {
            try {
                if (event.getItem().getItemStack().getType() == Material.WOOL && event.getItem().getItemStack().getData().getData() == color.getData()) {
                    if (TeamUtils.getTeamByPlayer(player) == team) {
                        boolean touchMessage = false;
                        if (!this.playersTouched.contains(player.getUniqueId())) {
                            this.playersTouched.add(player.getUniqueId());
                            if (this.show && !this.complete) {
                                TeamUtils.getTeamChannel(team).sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY)));
                                for (Player player1 : Bukkit.getOnlinePlayers()) {
                                    if (TeamUtils.getTeamByPlayer(player1) != null && TeamUtils.getTeamByPlayer(player1).isObserver()) {
                                        player1.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY, team.getCompleteName() + ChatColor.GRAY)).getMessage(player1.getLocale()));
                                    }
                                }
                                touchMessage = true;
                            }
                        }
                        boolean oldState = this.touched;
                        this.touched = true;
                        if (touchMessage) {
                            double newProx;
                            if (location != null) {
                                newProx = location.distance(place.getVector());
                            } else {
                                newProx = player.getLocation().toVector().distance(place.getVector());
                            }
                            if (!oldState || newProx < proximity) {
                                proximity = newProx;
                            }
                        }
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, !oldState, touchMessage);
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
                        if (this.show) ChatUtils.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PLACED, team.getColor() + event.getPlayer().getName() + ChatColor.WHITE, team.getCompleteName() + ChatColor.WHITE, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.WHITE)));
                        FireworkUtil.spawnFirework(event.getPlayer().getLocation(), event.getPlayer().getWorld(), MiscUtils.convertChatColorToColor(MiscUtils.convertDyeColorToChatColor(color)));
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        if (this.show) ChatUtils.sendWarningMessage(event.getPlayer(), "You may not complete the other team's objective.");
                    }
                } else {
                    event.setCancelled(true);
                    if (this.show) ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtils.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
                }
            } else {
                event.setCancelled(true);
                if (this.show) ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtils.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraftWool(CraftItemEvent event) {
        if (event.getRecipe().getResult().equals(new ItemStack(Material.WOOL, 1, color.getData())) && !this.craftable) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (event.getKiller() != null && location != null && GameHandler.getGameHandler().getMatch().isRunning() && !this.touched && TeamUtils.getTeamByPlayer(event.getKiller()) != null && TeamUtils.getTeamByPlayer(event.getKiller()) == this.team) {
            if (event.getKiller().getLocation().toVector().distance(location) < proximity) {
                double old = proximity;
                proximity = event.getKiller().getLocation().toVector().distance(location);
                Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(this, event.getKiller(), old, proximity));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSafetyPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && this.touched) {
            if (event.getBlock().getType().equals(Material.WOOL)) {
                if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    if (TeamUtils.getTeamByPlayer(event.getPlayer()) == team) {
                        if (event.getBlockPlaced().getLocation().distance(place.getLocation()) < proximity) {
                            double old = proximity;
                            proximity = event.getBlockPlaced().getLocation().distance(place.getLocation());
                            Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(this, event.getPlayer(), old, proximity));
                        }
                    }
                }
            }
        }
    }

    public double getProximity() {
        return proximity;
    }

    public boolean showProximity() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTimeLimit() != 0 && GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.MOST_OBJECTIVES);
    }

    @EventHandler
    public void onWoolTouch(ObjectiveTouchEvent event) {
        if (event.getObjective().equals(this) && event.displayTouchMessage()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getPlayer(), Snowflakes.ChangeReason.WOOL_TOUCH, 8, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY));
        }
    }

    @EventHandler
    public void onWoolPlace(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this) && event.getObjective().showOnScoreboard()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getPlayer(), Snowflakes.ChangeReason.WOOL_PLACE, 15, MiscUtils.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY));
        }
    }
}
