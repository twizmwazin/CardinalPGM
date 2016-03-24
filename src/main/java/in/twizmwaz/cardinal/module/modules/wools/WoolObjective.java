package in.twizmwaz.cardinal.module.modules.wools;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
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
    private final boolean required;

    private GameObjectiveProximityHandler wool;
    private GameObjectiveProximityHandler monument;

    private Set<UUID> playersTouched;
    private boolean touched;
    private boolean complete;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected WoolObjective(final TeamModule team, final String name, final String id, final DyeColor color, final BlockRegion place, final boolean craftable,
                            final boolean show, final boolean required, GameObjectiveProximityHandler woolProximity, GameObjectiveProximityHandler monumentProximity) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.color = color;
        this.place = place;
        this.craftable = craftable;
        this.show = show;
        this.required = required;

        this.playersTouched = new HashSet<>();

        this.scoreboardHandler = new GameObjectiveScoreboardHandler(this);

        this.wool = woolProximity;
        this.wool.setObjective(this);
        this.monument = monumentProximity;
        this.monument.setObjective(this);
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

    @Override
    public boolean isRequired() {
        return required;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @Override
    public GameObjectiveProximityHandler getProximityHandler() {
        return touched ? monument: wool;
    }

    @EventHandler
    public void onWoolPickup(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.complete && GameHandler.getGameHandler().getMatch().isRunning()) {
            try {
                if (event.getCurrentItem().getType() == Material.WOOL && event.getCurrentItem().getData().getData() == color.getData()) {
                    if (Teams.getTeamByPlayer(player).orNull() == team) {
                        boolean touchMessage = false;
                        if (!this.playersTouched.contains(player.getUniqueId())) {
                            this.playersTouched.add(player.getUniqueId());
                            if (this.show && !this.complete) {
                                Teams.getTeamChannel(Optional.of(team)).sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.WHITE, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.WHITE, team.getCompleteName() + ChatColor.WHITE)));
                                for (Player player1 : Bukkit.getOnlinePlayers()) {
                                    if (Teams.getTeamByPlayer(player1).isPresent() && Teams.getTeamByPlayer(player1).get().isObserver()) {
                                        player1.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY, team.getCompleteName() + ChatColor.GRAY)).getMessage(player1.getLocale()));
                                    }
                                }
                                touchMessage = true;
                            }
                        }
                        if (!touched) touched = true;
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, touchMessage);
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
                    if (Teams.getTeamByPlayer(player).orNull() == team) {
                        boolean touchMessage = false;
                        if (!this.playersTouched.contains(player.getUniqueId())) {
                            this.playersTouched.add(player.getUniqueId());
                            if (this.show && !this.complete) {
                                Teams.getTeamChannel(Optional.of(team)).sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED, team.getColor() + player.getName() + ChatColor.WHITE, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.WHITE, team.getCompleteName() + ChatColor.WHITE)));
                                for (Player player1 : Bukkit.getOnlinePlayers()) {
                                    if (Teams.getTeamByPlayer(player1).isPresent() && Teams.getTeamByPlayer(player1).get().isObserver()) {
                                        player1.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PICKED_FOR, team.getColor() + player.getName() + ChatColor.GRAY, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY, team.getCompleteName() + ChatColor.GRAY)).getMessage(player1.getLocale()));
                                    }
                                }
                                touchMessage = true;
                            }
                        }
                        if (!touched) touched = true;
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, player, touchMessage);
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
                    if (Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {
                        this.complete = true;
                        if (this.show)
                            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.WHITE + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_PLACED, team.getColor() + event.getPlayer().getName() + ChatColor.WHITE, team.getCompleteName() + ChatColor.WHITE, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.WHITE)));
                        Fireworks.spawnFirework(event.getPlayer().getLocation(), event.getPlayer().getWorld(), MiscUtil.convertChatColorToColor(MiscUtil.convertDyeColorToChatColor(color)));
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                        event.setCancelled(false);
                    } else {
                        event.setCancelled(true);
                        if (this.show)
                            ChatUtil.sendWarningMessage(event.getPlayer(), "You may not complete the other team's objective.");
                    }
                } else {
                    event.setCancelled(true);
                    if (this.show)
                        ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtil.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
                }
            } else {
                event.setCancelled(true);
                if (this.show)
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_BLOCK_PLACE, MiscUtil.convertDyeColorToChatColor(color) + color.name().toUpperCase().replaceAll("_", " ") + " WOOL" + ChatColor.RED));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().equals(place.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonPush(BlockPistonExtendEvent event) {
        if (!event.isCancelled()) {
            if (event.getBlock().getRelative(event.getDirection()).equals(place.getBlock())) {
                event.setCancelled(true);
            } else {
                for (Block block : event.getBlocks()) {
                    if (block.equals(place.getBlock()) || block.equals(place.getBlock().getRelative(event.getDirection().getOppositeFace()))) {
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
                if (block.equals(place.getBlock()) || block.equals(place.getBlock().getRelative(event.getDirection().getOppositeFace()))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (place.getBlock().equals(event.getBlock()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCraftWool(CraftItemEvent event) {
        if (event.getRecipe().getResult().equals(new ItemStack(Material.WOOL, 1, color.getData())) && !this.craftable) {
            event.setCancelled(true);
        }
    }

    public Double getProximity() {
        return getProximityHandler().getProximity();
    }

    public boolean showProximity() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTimeLimit() != 0 && GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.MOST_OBJECTIVES);
    }

    @EventHandler
    public void onWoolTouch(ObjectiveTouchEvent event) {
        if (event.getObjective().equals(this) && event.displayTouchMessage()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getPlayer(), Snowflakes.ChangeReason.WOOL_TOUCH, 8, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY));
        }
    }

    @EventHandler
    public void onWoolPlace(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this) && event.getObjective().showOnScoreboard()) {
            Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(event.getPlayer(), Snowflakes.ChangeReason.WOOL_PLACE, 15, MiscUtil.convertDyeColorToChatColor(color) + name.toUpperCase().replaceAll("_", " ") + ChatColor.GRAY));
        }
    }
}