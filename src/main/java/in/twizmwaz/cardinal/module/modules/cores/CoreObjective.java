package in.twizmwaz.cardinal.module.modules.cores;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannel;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.snowflakes.Snowflakes;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Fireworks;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CoreObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final RegionModule region;
    private final int leak;
    private final boolean show;
    private final boolean required;
    private boolean changesModes;

    private Map<String, GameObjectiveProximityHandler> proximityHandlers;

    private Set<UUID> playersTouched;
    private Set<UUID> playersCompleted;
    private Pair<Material, Integer> material;
    private Set<Block> lava;
    private Set<Block> core;

    private boolean touched;
    private boolean complete;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected CoreObjective(final TeamModule team, final String name, final String id, final RegionModule region, final int leak, Pair<Material, Integer> material,
                            final boolean show, final boolean required, boolean changesModes, Map<String, GameObjectiveProximityHandler> proximityHandlers) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.leak = leak;
        this.show = show;
        this.required = required;
        this.changesModes = changesModes;

        this.proximityHandlers = proximityHandlers;
        for (GameObjectiveProximityHandler proximityHandler : proximityHandlers.values()) {
            proximityHandler.setObjective(this);
        }

        this.playersTouched = new HashSet<>();
        this.playersCompleted = new HashSet<>();
        this.material = material;

        this.lava = new HashSet<>();
        this.core = new HashSet<>();
        for (Block block : region.getBlocks()) {
            if (partOfObjective(block)) {
                core.add(block);
            }
            if (block.getType().equals(Material.STATIONARY_LAVA) || block.getType().equals(Material.LAVA)) {
                lava.add(block);
            }
        }

        this.scoreboardHandler = new GameObjectiveScoreboardHandler(this);
    }

    public static CoreObjective getClosestCore(double x, double y, double z) {
        CoreObjective core = null;
        double closestDistance = Double.POSITIVE_INFINITY;
        for (Module module : GameHandler.getGameHandler().getMatch().getModules()) {
            if (module instanceof CoreObjective) {
                BlockRegion center = ((CoreObjective) module).getRegion().getCenterBlock();
                if (new Vector(x, y, z).distance(new Vector(center.getX(), center.getY(), center.getZ())) < closestDistance) {
                    core = (CoreObjective) module;
                    closestDistance = new Vector(x, y, z).distance(new Vector(center.getX(), center.getY(), center.getZ()));
                }
            }
        }
        return core;
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
        return name;
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

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @Override
    public GameObjectiveProximityHandler getProximityHandler(TeamModule team) {
        return isTouched() || isComplete() ? null : proximityHandlers.get(team.getId());
    }

    @EventHandler
    public void onObsidianForm(BlockFormEvent event) {
        if (this.lava.contains(event.getBlock())) {
            if (event.getNewState().getType().equals(Material.OBSIDIAN)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (lava.contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (getBlocks().contains(event.getBlock())) {
                if (Teams.getTeamByPlayer(event.getPlayer()).orNull() != team) {
                    boolean touchMessage = false;
                    if (!playersTouched.contains(event.getPlayer().getUniqueId())) {
                        playersTouched.add(event.getPlayer().getUniqueId());
                        Optional<TeamModule> teamModule = Teams.getTeamByPlayer(event.getPlayer());
                        if (teamModule.isPresent()) {
                            ChatChannel channel = Teams.getTeamChannel(teamModule);
                            if (this.show && !this.complete) {
                                channel.sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_TOUCHED_FOR, teamModule.get().getColor() + event.getPlayer().getName() + ChatColor.WHITE, name, teamModule.get().getCompleteName() + ChatColor.WHITE));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver()) {
                                        player.sendMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_TOUCHED_FOR, teamModule.get().getColor() + event.getPlayer().getName() + ChatColor.GRAY, ChatColor.RED + name + ChatColor.GRAY, teamModule.get().getCompleteName() + ChatColor.GRAY).getMessage(player.getLocale()));
                                    }
                                }
                                touchMessage = true;
                            }
                        }
                    }
                    if (!playersCompleted.contains(event.getPlayer().getUniqueId()))
                        playersCompleted.add(event.getPlayer().getUniqueId());
                    this.touched = true;
                    ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), touchMessage);
                    Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                    if (this.show)
                        ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_OWN_CORE));
                    return;
                }
            }
            if (core.contains(event.getBlock())) {
                if (Teams.getTeamByPlayer(event.getPlayer()).orNull() == team) {
                    event.setCancelled(true);
                    if (this.show)
                        ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_OWN_CORE));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            List<Block> objectiveBlownUp = new ArrayList<>();
            for (Block block : event.blockList()) {
                if (getBlocks().contains(block) || core.contains(block)) {
                    objectiveBlownUp.add(block);
                }
            }
            boolean blownUp = false;
            Player eventPlayer = null;
            boolean touchMessage = false;
            for (Block block : objectiveBlownUp) {
                if (TntTracker.getWhoPlaced(event.getEntity()) != null) {
                    UUID player = TntTracker.getWhoPlaced(event.getEntity());
                    if (Bukkit.getOfflinePlayer(player).isOnline()) {
                        if (Teams.getTeamByPlayer(Bukkit.getPlayer(player)).orNull() == team || (Teams.getTeamByPlayer(Bukkit.getPlayer(player)).isPresent() && Teams.getTeamByPlayer(Bukkit.getPlayer(player)).get().isObserver())) {
                            event.blockList().remove(block);
                        } else {
                            if (!playersTouched.contains(player)) {
                                playersTouched.add(player);
                                Optional<TeamModule> teamModule = Teams.getTeamByPlayer(Bukkit.getPlayer(player));
                                ChatChannel channel = Teams.getTeamChannel(teamModule);
                                //TODO: Support players not on teams
                                if (this.show && !this.complete) {
                                    channel.sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_TOUCHED_FOR, teamModule.get().getColor() + Bukkit.getPlayer(player).getName() + ChatColor.WHITE, name, teamModule.get().getCompleteName() + ChatColor.WHITE));

                                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                                        if (Teams.getTeamByPlayer(player1) != null && Teams.getTeamByPlayer(player1).get().isObserver()) {
                                            player1.sendMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_TOUCHED_FOR, teamModule.get().getColor() + Bukkit.getPlayer(player).getName() + ChatColor.GRAY, ChatColor.RED + name + ChatColor.GRAY, teamModule.get().getCompleteName() + ChatColor.GRAY).getMessage(player1.getLocale()));
                                        }
                                    }
                                    touchMessage = true;
                                }
                            }
                            if (!playersCompleted.contains(Bukkit.getPlayer(player).getUniqueId()))
                                playersCompleted.add(Bukkit.getPlayer(player).getUniqueId());
                            this.touched = true;
                            blownUp = true;
                            eventPlayer = Bukkit.getPlayer(player);
                        }
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
                        }
                        this.touched = true;
                        blownUp = true;
                    }
                } else {
                    this.touched = true;
                    blownUp = true;
                }
            }
            if (!this.complete && blownUp) {
                ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, touchMessage);
                Bukkit.getServer().getPluginManager().callEvent(touchEvent);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (lava.contains(event.getToBlock())){
            event.setCancelled(true);
        }
        if (!event.isCancelled()) {
            Block to = event.getToBlock();
            Block from = event.getBlock();
            if (CoreObjective.getClosestCore(to.getX(), to.getY(), to.getZ()).equals(this)) {
                if ((from.getType().equals(Material.LAVA) || from.getType().equals(Material.STATIONARY_LAVA)) && to.getType().equals(Material.AIR)) {
                    double minY = 256;
                    for (Block block : getCore()) {
                        if (block.getY() < minY)
                            minY = block.getY();
                    }
                    if (minY - to.getY() >= leak && !this.complete) {
                        this.complete = true;
                        event.setCancelled(false);
                        if (this.show)
                            ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_LEAKED, team.getCompleteName() + ChatColor.RED, name)));
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, null);
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                    }
                }
            }
        }
    }
     
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonPush(BlockPistonExtendEvent event) {
        if (!event.isCancelled()) {
            for (Block block : event.getBlocks()) {
                if (getBlocks().contains(block)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        if (!event.isCancelled() && event.isSticky()) {
            for (Block block : event.getBlocks()) {
                if (getBlocks().contains(block)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getUniqueId())) {
            playersTouched.remove(event.getEntity().getUniqueId());
        }
    }

    public RegionModule getRegion() {
        return region;
    }

    public Set<Block> getCore() {
        return core;
    }

    public boolean partOfObjective(Block block) {
        return material.getLeft().equals(block.getType()) && (material.getRight() == -1 || material.getRight() == (int) block.getState().getData().getData());
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (Block block : region.getBlocks()) {
            if (partOfObjective(block)) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    public boolean showProximity(TeamModule team) {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTimeLimit() != 0 &&
                GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.MOST_OBJECTIVES) &&
                ((team.isObserver() && proximityHandlers.size() == 1) || (!team.isObserver() && !team.equals(this.team)));
    }

    public boolean changesModes() {
        return changesModes;
    }

    public void setChangesModes(boolean changesModes) {
        this.changesModes = changesModes;
    }

    public void setMaterial(Material material, int damageValue) {
        this.material = new ImmutablePair<>(material, damageValue);
    }

    public Double getProximity(TeamModule team) {
        return team.isObserver() ? (proximityHandlers.size() == 1 ? proximityHandlers.values().iterator().next().getProximity() : null) : (team.equals(this.team) ? null : proximityHandlers.get(team.getId()).getProximity());
    }

    @EventHandler
    public void onCoreLeak(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this) && event.getObjective().showOnScoreboard()) {
            Fireworks.spawnFireworks(region.getCenterBlock().getAlignedVector(), (region.getMax().minus(region.getMin()).length()) * 0.55 + 1, 6, MiscUtil.convertChatColorToColor(team.getColor()), 1);
            for (UUID player : playersCompleted) {
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(Bukkit.getPlayer(player), Snowflakes.ChangeReason.CORE_LEAK, 15, ChatColor.RED + name + ChatColor.GRAY));
                }
            }
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (lava.contains(event.getBlockClicked()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (lava.contains(event.getBlockClicked().getRelative(event.getBlockFace())))
            event.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (lava.contains(event.getBlock()))
            event.setCancelled(true);
    }
}
