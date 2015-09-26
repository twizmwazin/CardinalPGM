package in.twizmwaz.cardinal.module.modules.wools;

import com.google.common.base.Optional;
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
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Fireworks;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WoolObjective implements GameObjective {

    private final String id;
    private final boolean required;
    private final TeamModule team;
    private final DyeColor color;
    private final BlockRegion monument;
    private final boolean craftable;
    private final boolean show;
    private final Vector location;
    private final ProximityMetric woolProximityMetric;
    private final boolean woolProximityHorizontal;
    private final ProximityMetric monumentProximityMetric;
    private final boolean monumentProximityHorizontal;

    private Set<UUID> playersTouched;
    private double proximity;
    private GameObjectiveScoreboardHandler scoreboardHandler;

    private boolean touched;
    private boolean complete;


    protected WoolObjective(final String id, final boolean required, final TeamModule team, final DyeColor color, final BlockRegion monument, final boolean craftable, final boolean show, final Vector location, final ProximityMetric woolProximityMetric, final boolean woolProximityHorizontal, final ProximityMetric monumentProximityMetric, final boolean monumentProximityHorizontal) {
        this.id = id;
        this.required = required;
        this.team = team;
        this.color = color;
        this.monument = monument;
        this.craftable = craftable;
        this.show = show;
        this.location = location;
        this.woolProximityMetric = woolProximityMetric;
        this.woolProximityHorizontal = woolProximityHorizontal;
        this.monumentProximityMetric = monumentProximityMetric;
        this.monumentProximityHorizontal = monumentProximityHorizontal;


        this.playersTouched = new HashSet<>();
        this.proximity = Double.POSITIVE_INFINITY;
        this.scoreboardHandler = new GameObjectiveScoreboardHandler(this);
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public String getName() {
        return color == null ? "Wool" : color.name() + " Wool";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public TeamModule getTeam() {
        return team;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public boolean show() {
        return show;
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
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    // Used for keeping track of when players touch a wool by taking it from an inventory.
    @EventHandler
    public void onWoolPickup(InventoryClickEvent event) {
        handleWoolPickup((Player) event.getWhoClicked(), event.getCurrentItem());
    }

    // Used for keeping track of when players touch a wool by picking it up off of the ground.
    @EventHandler
    public void onWoolPickup(PlayerPickupItemEvent event) {
        handleWoolPickup(event.getPlayer(), event.getItem().getItemStack());
    }

    private void handleWoolPickup(Player player, ItemStack item) {
        if (GameHandler.getGameHandler().getMatch().isRunning() && !complete &&
                item != null && item.getType().equals(Material.WOOL) &&
                item.getData().getData() == color.getData() &&
                Teams.getTeamByPlayer(player).orNull() == team) {

            boolean touchMessage = false;
            if (!playersTouched.contains(player.getUniqueId())) {
                playersTouched.add(player.getUniqueId());
                if (show) {
                    Teams.getTeamChannel(Optional.of(team)).sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.WHITE, MiscUtil.convertDyeColorToChatColor(color) + getName().toUpperCase().replaceAll("_", " ") + ChatColor.WHITE, team.getCompleteName() + ChatColor.WHITE)));
                    Teams.getTeamChannel(Teams.getTeamById("observers")).sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtil.convertDyeColorToChatColor(color) + getName().toUpperCase().replaceAll("_", " ") + ChatColor.GRAY, team.getCompleteName() + ChatColor.GRAY)));
                    touchMessage = true;
                }
            }
            if (!touched) {
                proximity = monumentProximityMetric.equals(ProximityMetric.CLOSEST_PLAYER) ? MiscUtil.getDistance(player.getLocation().toVector(), monument.getVector(), monumentProximityHorizontal) : Double.POSITIVE_INFINITY;
            }
            ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, !touched, touchMessage);
            touched = true;
            Bukkit.getServer().getPluginManager().callEvent(touchEvent);

        }
    }

    // Used for keeping track of when players move to a location closer to a proximity location.
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (touched) {
            handleMovementProximity(monumentProximityMetric, monumentProximityHorizontal, event.getPlayer(), monument.getVector());
        } else if (location != null) {
            handleMovementProximity(woolProximityMetric, woolProximityHorizontal, event.getPlayer(), location);
        }
    }

    private void handleMovementProximity(ProximityMetric proximityMetric, boolean proximityHorizontal, Player player, Vector location) {
        if (proximityMetric.equals(ProximityMetric.CLOSEST_PLAYER) && location != null) {
            double distance = MiscUtil.getDistance(player.getLocation().toVector(), location, proximityHorizontal);
            if (distance < proximity) {
                ObjectiveProximityEvent proximityEvent = new ObjectiveProximityEvent(this, player, proximity, distance);
                proximity = distance;
                Bukkit.getServer().getPluginManager().callEvent(proximityEvent);
            }
        }
    }

    // Used for keeping track of when players place a block on the ground at a location closer to the proximity location.
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlaceProximity(BlockPlaceEvent event) {
        if (!event.isCancelled() && Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {
            if (touched && monumentProximityMetric.equals(ProximityMetric.CLOSEST_BLOCK)) {
                if (event.getBlock().getType().equals(Material.WOOL) && ((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    double distance = MiscUtil.getDistance(event.getBlockPlaced().getLocation(), monument.getLocation(), monumentProximityHorizontal);
                    if (distance < proximity) {
                        ObjectiveProximityEvent proximityEvent = new ObjectiveProximityEvent(this, event.getPlayer(), proximity, distance);
                        proximity = distance;
                        Bukkit.getServer().getPluginManager().callEvent(proximityEvent);
                    }
                }
            } else if (location != null && woolProximityMetric.equals(ProximityMetric.CLOSEST_BLOCK)) {
                double distance = MiscUtil.getDistance(event.getBlockPlaced().getLocation(), location, woolProximityHorizontal);
                if (distance < proximity) {
                    ObjectiveProximityEvent proximityEvent = new ObjectiveProximityEvent(this, event.getPlayer(), proximity, distance);
                    proximity = distance;
                    Bukkit.getServer().getPluginManager().callEvent(proximityEvent);
                }
            }
        }
    }

    // Used for keeping track of when players kill their opponents at a location closer to the proximity location
    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        Player killer = event.getKiller();
        if (GameHandler.getGameHandler().getMatch().isRunning() &&
                killer != null &&
                Teams.getTeamByPlayer(killer).orNull() == this.team) {

            if (touched && monumentProximityMetric.equals(ProximityMetric.CLOSEST_KILL)) {
                double distance = MiscUtil.getDistance(killer.getLocation(), monument.getVector(), monumentProximityHorizontal);
                if (distance < proximity) {
                    ObjectiveProximityEvent proximityEvent = new ObjectiveProximityEvent(this, event.getKiller(), proximity, distance);
                    proximity = distance;
                    Bukkit.getServer().getPluginManager().callEvent(proximityEvent);
                }
            } else if (location != null && woolProximityMetric.equals(ProximityMetric.CLOSEST_KILL)) {
                double distance = MiscUtil.getDistance(killer.getLocation(), location, woolProximityHorizontal);
                if (distance < proximity) {
                    ObjectiveProximityEvent proximityEvent = new ObjectiveProximityEvent(this, event.getKiller(), proximity, distance);
                    proximity = distance;
                    Bukkit.getServer().getPluginManager().callEvent(proximityEvent);
                }
            }

        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        UUID uuid = event.getEntity().getUniqueId();
        while (playersTouched.contains(uuid)) {
            playersTouched.remove(uuid);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().equals(monument.getBlock())) {
            Player player = event.getPlayer();
            if (event.getBlock().getType().equals(Material.WOOL)) {
                if (((Wool) event.getBlock().getState().getData()).getColor().equals(color)) {
                    if (Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {
                        complete = true;
                        if (this.show) ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PLACED, team.getColor() + event.getPlayer().getName() + ChatColor.WHITE, team.getCompleteName() + ChatColor.WHITE, MiscUtil.convertDyeColorToChatColor(color) + getName().toUpperCase().replaceAll("_", " ") + ChatColor.WHITE)));
                        Fireworks.spawnFirework(player.getLocation(), player.getWorld(), MiscUtil.convertChatColorToColor(MiscUtil.convertDyeColorToChatColor(color)));
                        Bukkit.getServer().getPluginManager().callEvent(new ObjectiveCompleteEvent(this, player));
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        if (show) ChatUtil.sendWarningMessage(player, ChatConstant.ERROR_CANNOT_COMPLETE_OBJECTIVE.asMessage());
                    }
                } else {
                    event.setCancelled(true);
                    if (show) ChatUtil.sendWarningMessage(player, new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtil.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
                }
            } else {
                event.setCancelled(true);
                if (show) ChatUtil.sendWarningMessage(player, new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtil.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().equals(monument.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonPush(BlockPistonExtendEvent event) {
        if (!event.isCancelled()) {
            if (event.getBlock().getRelative(event.getDirection()).equals(monument.getBlock())) {
                event.setCancelled(true);
            } else {
                for (Block block : event.getBlocks()) {
                    if (block.equals(monument.getBlock()) || block.equals(monument.getBlock().getRelative(event.getDirection().getOppositeFace()))) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isCancelled()) {
            for (Block block : event.getBlocks()) {
                if (block.equals(monument.getBlock()) || block.equals(monument.getBlock().getRelative(event.getDirection().getOppositeFace()))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (monument.getBlock().equals(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCraftWool(CraftItemEvent event) {
        if (event.getRecipe().getResult().equals(new ItemStack(Material.WOOL, 1, color.getData())) && !craftable) {
            event.setCancelled(true);
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
        if (event.getObjective().equals(this) && event.hasTouchMessage()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getPlayer(), Snowflakes.ChangeReason.WOOL_TOUCH, 8, MiscUtil.convertDyeColorToChatColor(color) + getName().toUpperCase().replaceAll("_", " ") + ChatColor.GRAY));
        }
    }

    @EventHandler
    public void onWoolPlace(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this) && event.getObjective().show()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getPlayer(), Snowflakes.ChangeReason.WOOL_PLACE, 15, MiscUtil.convertDyeColorToChatColor(color) + getName().toUpperCase().replaceAll("_", " ") + ChatColor.GRAY));
        }
    }
}