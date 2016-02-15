package in.twizmwaz.cardinal.module.modules.destroyable;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.SnowflakeChangeEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveProximityEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.snowflakes.Snowflakes;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Fireworks;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class DestroyableObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final RegionModule region;
    private final double completion;
    private final boolean showPercent;
    private final boolean repairable;
    private final boolean show;
    private final boolean required;
    private List<Material> types;
    private List<Integer> damageValues;
    private boolean changesModes;

    private double proximity;

    private Set<UUID> playersTouched;
    private double size;
    private HashMap<UUID, Integer> playersCompleted;
    private List<Block> monument;

    private double complete;
    private boolean completed;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected DestroyableObjective(final TeamModule team, final String name, final String id, final RegionModule region, List<Material> types, List<Integer> damageValues, final double completion, final boolean show, final boolean required, boolean changesModes, boolean showPercent, boolean repairable) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.types = types;
        this.damageValues = damageValues;
        this.showPercent = showPercent;
        this.repairable = repairable;
        this.complete = 0;
        this.completion = completion;
        this.show = show;
        this.required = required;
        this.changesModes = changesModes;
        this.completed = false;

        this.proximity = Double.POSITIVE_INFINITY;

        this.playersTouched = new HashSet<>();
        this.playersCompleted = new HashMap<>();

        this.monument = this.getBlocks();
        this.size = this.getBlocks().size();

        this.scoreboardHandler = new GameObjectiveScoreboardHandler(this);
    }

    @Override
    public TeamModule getTeam() {
        return this.team;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isTouched() {
        return this.complete > 0;
    }

    @Override
    public boolean isComplete() {
        return this.completed;
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
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (getBlocks().contains(event.getBlock())) {
                Optional<TeamModule> playerTeam = Teams.getTeamByPlayer(event.getPlayer());
                if (playerTeam.isPresent() && playerTeam.get() != team) {
                    boolean touchMessage = false;
                    if (!playersTouched.contains(event.getPlayer().getUniqueId())) {
                        playersTouched.add(event.getPlayer().getUniqueId());
                        Optional<TeamModule> teamModule = Teams.getTeamByPlayer(event.getPlayer());
                        if (this.show && !this.completed && teamModule.isPresent()) {
                            Teams.getTeamChannel(teamModule).sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED_FOR, teamModule.get().getColor() + event.getPlayer().getName() + ChatColor.WHITE, name, teamModule.get().getCompleteName() + ChatColor.WHITE));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (Teams.getTeamByPlayer(player).isPresent() && Teams.getTeamByPlayer(player).get().isObserver()) {
                                    player.sendMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED_FOR, teamModule.get().getColor() + event.getPlayer().getName() + ChatColor.WHITE, name, teamModule.get().getCompleteName() + ChatColor.WHITE).getMessage(player.getLocale()));
                                }
                            }
                        }
                    }
                    boolean oldState = this.isTouched();
                    this.complete++;
                    this.playersCompleted.put(event.getPlayer().getUniqueId(), (playersCompleted.containsKey(event.getPlayer().getUniqueId()) ? playersCompleted.get(event.getPlayer().getUniqueId()) + 1 : 1));
                    if ((this.complete / size) >= this.completion && !this.completed) {
                        this.completed = true;
                        event.setCancelled(false);
                        if (this.show) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                player.sendMessage(ChatColor.WHITE + new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, team.getCompleteName() + ChatColor.WHITE, name, getWhoDestroyed(player.getLocale()))).getMessage(player.getLocale()));
                        }
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                    } else if (!this.completed) {
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), !oldState || showPercent, touchMessage);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                } else {
                    event.setCancelled(true);
                    if (this.show)
                        ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_OWN_OBJECTIVE));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!event.isCancelled()) {
            List<Block> objectiveBlownUp = new ArrayList<>();
            for (Block block : event.blockList()) {
                if (getBlocks().contains(block)) {
                    objectiveBlownUp.add(block);
                }
            }
            boolean oldState = this.isTouched();
            boolean blownUp = false;
            Player eventPlayer = null;
            int originalPercent = getPercent();
            boolean touchMessage = false;
            for (Block block : objectiveBlownUp) {
                boolean blockDestroyed = false;
                if (TntTracker.getWhoPlaced(event.getEntity()) != null) {
                    UUID playerID = TntTracker.getWhoPlaced(event.getEntity());
                    if (Bukkit.getOfflinePlayer(playerID).isOnline()) {
                        Player player = Bukkit.getPlayer(playerID);
                        if (Teams.getTeamByPlayer(Bukkit.getPlayer(playerID)).orNull() == team || (Teams.getTeamByPlayer(Bukkit.getPlayer(playerID)).isPresent() && Teams.getTeamByPlayer(Bukkit.getPlayer(playerID)).get().isObserver())) {
                            event.blockList().remove(block);
                        } else {
                            if (!playersTouched.contains(playerID)) {
                                playersTouched.add(playerID);
                                Optional<TeamModule> teamModule = Teams.getTeamByPlayer(player);
                                if (this.show && !this.completed) {
                                    if (!teamModule.isPresent() || !teamModule.get().isObserver()) {
                                        Teams.getTeamChannel(teamModule).sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED_FOR, Teams.getTeamColorByPlayer(Bukkit.getPlayer(playerID)) + Bukkit.getPlayer(playerID).getName() + ChatColor.WHITE, name, teamModule.get().getCompleteName() + ChatColor.WHITE));
                                    }
                                    for (Player player1 : Bukkit.getOnlinePlayers()) {
                                        Optional<TeamModule> team1 = Teams.getTeamByPlayer(player1);
                                        if (team1.isPresent() && team1.get().isObserver()) {
                                            player1.sendMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED_FOR, teamModule.get().getColor() + Bukkit.getPlayer(playerID).getName() + ChatColor.WHITE, name, teamModule.get().getCompleteName() + ChatColor.WHITE).getMessage(player1.getLocale()));
                                        }
                                    }
                                    touchMessage = true;
                                }
                            }
                            blockDestroyed = true;
                            blownUp = true;
                            eventPlayer = Bukkit.getPlayer(playerID);
                        }
                    } else {
                        if (!playersTouched.contains(playerID)) {
                            playersTouched.add(playerID);
                        }
                        blockDestroyed = true;
                        blownUp = true;
                    }
                } else {
                    blockDestroyed = true;
                    blownUp = true;
                }
                if (blockDestroyed) {
                    this.complete++;
                    if (eventPlayer != null)
                        this.playersCompleted.put(eventPlayer.getUniqueId(), (playersCompleted.containsKey(eventPlayer.getUniqueId()) ? playersCompleted.get(eventPlayer.getUniqueId()) + 1 : 1));
                    if ((this.complete / size) >= this.completion && !this.completed) {
                        this.completed = true;
                        if (this.show) {
                            for (Player player : Bukkit.getOnlinePlayers())
                                player.sendMessage(ChatColor.WHITE + new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, team.getCompleteName() + ChatColor.WHITE, name, getWhoDestroyed(player.getLocale()))).getMessage(player.getLocale()));
                            Bukkit.getConsoleSender().sendMessage(ChatColor.WHITE + new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, team.getCompleteName() + ChatColor.WHITE, name, getWhoDestroyed(Locale.getDefault().toString()))).getMessage(Locale.getDefault().toString()));
                        }
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, eventPlayer);
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                    }
                }
            }
            if (!this.completed && blownUp) {
                ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, !oldState || (getPercent() != originalPercent), touchMessage);
                Bukkit.getServer().getPluginManager().callEvent(touchEvent);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!repairable && !isComplete()) {
            if (region.contains(event.getBlock().getLocation()) && partOfObjective(event.getBlock())) {
                if (Teams.getTeamByPlayer(event.getPlayer()).orNull() != team) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_ENEMY_OBJECTIVE));
                    event.setCancelled(true);
                } else {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_REPAIR_OBJECTIVE));
                    event.setCancelled(true);
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

    public int getBlocksRequired() {
        return (int) Math.ceil(size * completion);
    }

    public int getBlocksBroken() {
        return (int) complete;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getUniqueId())) {
            playersTouched.remove(event.getEntity().getUniqueId());
        }
    }

    public boolean showPercent() {
        return showPercent;
    }

    public boolean showProximity() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getTimeLimit() != 0 && GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).getResult().equals(TimeLimit.Result.MOST_OBJECTIVES);
    }

    public boolean isRepairable() {
        return repairable;
    }

    public int getPercent() {
        double blocksRequired = completion * size;
        if (Math.floor((complete / blocksRequired) * 100) > 100) {
            return 100;
        }
        if (Math.floor((complete / blocksRequired) * 100) < 0) {
            return 0;
        }
        return (int) Math.floor((complete / blocksRequired) * 100);
    }

    public int getPercentFromAmount(int amount) {
        double blocksRequired = completion * size;
        if (Math.floor((amount / blocksRequired) * 100) > 100) {
            return 100;
        }
        if (Math.floor((amount / blocksRequired) * 100) < 0) {
            return 0;
        }
        return (int) Math.floor((amount / blocksRequired) * 100);
    }

    public boolean partOfObjective(Block block) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).equals(block.getType()) && (damageValues.get(i) == -1 || damageValues.get(i) == (int) block.getState().getData().getData())) {
                return true;
            }
        }
        return false;
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

    public double getRadius() {
        List<Block> blocks = region.getBlocks();
        Vector min = blocks.get(0).getLocation();
        Vector max = blocks.get(blocks.size() - 1).getLocation();
        max.setY(min.getY());
        return min.distance(max) / 2;
    }

    public List<Block> getMonument() {
        return monument;
    }

    public String getWhoDestroyed(String locale) {
        String whoDestroyed = "";
        List<String> toCombine = new ArrayList<>();
        for (UUID player : MiscUtil.getSortedHashMapKeyset(playersCompleted)) {
            if (getPercentFromAmount(playersCompleted.get(player)) > (100 / 3)) {
                toCombine.add(Teams.getTeamColorByPlayer(Bukkit.getPlayer(player)) + Bukkit.getPlayer(player).getName() + ChatColor.WHITE + " (" + ChatColor.AQUA + getPercentFromAmount(playersCompleted.get(player)) + ChatColor.WHITE + "%)");
            }
        }
        if (toCombine.size() == 0) {
            toCombine.add(new LocalizedChatMessage(ChatConstant.MISC_ENEMY).getMessage(locale));
        }
        if (toCombine.size() < playersCompleted.keySet().size()) {
            toCombine.add(new LocalizedChatMessage(ChatConstant.MISC_OTHERS).getMessage(locale));
        }
        whoDestroyed = toCombine.get(0);
        for (int i = 1; i < toCombine.size(); i++) {
            whoDestroyed += ChatColor.WHITE + (i == toCombine.size() - 1 ? " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " " : ", ") + toCombine.get(i);
        }
        return whoDestroyed;
    }

    public boolean changesModes() {
        return changesModes;
    }

    public void setMaterial(Material material, int damageValue) {
        this.types = new ArrayList<>();
        this.damageValues = new ArrayList<>();
        this.types.add(material);
        this.damageValues.add(damageValue);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Optional<TeamModule> playerTeam = Teams.getTeamByPlayer(event.getPlayer());
        if (GameHandler.getGameHandler().getMatch().isRunning() && !this.isTouched() && ((playerTeam.isPresent() && !playerTeam.get().isObserver() && playerTeam.orNull() != this.team) || !playerTeam.isPresent())) {
            if (event.getPlayer().getLocation().toVector().distance(region.getCenterBlock().getVector()) < proximity) {
                double old = proximity;
                proximity = event.getPlayer().getLocation().toVector().distance(region.getCenterBlock().getVector());
                Bukkit.getServer().getPluginManager().callEvent(new ObjectiveProximityEvent(this, event.getPlayer(), old, proximity));
            }
        }
    }

    @EventHandler
    public void onMonumentDestroy(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this)) {
            Fireworks.spawnFireworks(region.getCenter().subtract(0.5,0.5,0.5), getRadius() * 1.1 + 1, 6, MiscUtil.convertChatColorToColor(team.getColor()), 1);
            for (UUID player : playersCompleted.keySet()) {
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(Bukkit.getPlayer(player), Snowflakes.ChangeReason.MONUMENT_DESTROY, getPercentFromAmount(playersCompleted.get(player)) / 10, ChatColor.GREEN + "" + getPercentFromAmount(playersCompleted.get(player)) + ChatColor.GRAY, ChatColor.GREEN + name + ChatColor.GRAY));
                }
            }
        }
    }

    public double getProximity() {
        return proximity;
    }
}
