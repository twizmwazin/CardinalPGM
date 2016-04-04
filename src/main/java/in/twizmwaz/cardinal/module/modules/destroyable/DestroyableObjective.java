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
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.snowflakes.Snowflakes;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.titleRespawn.TitleRespawn;
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
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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
    private List<Pair<Material, Integer>> materials;
    private boolean changesModes;

    private GameObjectiveProximityHandler proximityHandler;

    private Set<UUID> playersTouched;
    private double size;
    private HashMap<UUID, Integer> playersCompleted;
    private List<Block> monument;

    private double complete;
    private boolean completed;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected DestroyableObjective(final TeamModule team, final String name, final String id, final RegionModule region, List<Pair<Material, Integer>> materials, final double completion,
                                   final boolean show, final boolean required, boolean changesModes, boolean showPercent, boolean repairable, GameObjectiveProximityHandler proximityHandler) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.materials = materials;
        this.showPercent = showPercent;
        this.repairable = repairable;
        this.complete = 0;
        this.completion = completion;
        this.show = show;
        this.required = required;
        this.changesModes = changesModes;
        this.completed = false;

        this.proximityHandler = proximityHandler;
        this.proximityHandler.setObjective(this);
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

    @Override
    public GameObjectiveProximityHandler getProximityHandler() {
        return isTouched() || isComplete() ? null : proximityHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (getBlocks().contains(event.getBlock())) {
                Optional<TeamModule> playerTeam = Teams.getTeamByPlayer(event.getPlayer());
                if (playerTeam.isPresent() && playerTeam.get() != team) {
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
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), false);
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
                if (region.contains(block.getLocation()) && partOfObjective(block)) {
                    objectiveBlownUp.add(block);
                }
            }
            boolean blownUp = false;
            Player eventPlayer = null;
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
                }
            }
            if (blownUp) {
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
                if (!this.completed) {
                    ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, touchMessage);
                    Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!isComplete()) {
            if (monument.contains(event.getBlock()) && partOfObjective(event.getBlockPlaced())) {
                if (Teams.getTeamByPlayer(event.getPlayer()).orNull() != team) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_ENEMY_OBJECTIVE));
                    event.setCancelled(true);
                } else if (!isRepairable()) {
                    ChatUtil.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_REPAIR_OBJECTIVE));
                    event.setCancelled(true);
                } else {
                    complete--;
                    ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, null, false);
                    Bukkit.getServer().getPluginManager().callEvent(touchEvent);
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
        for (Pair<Material, Integer> material : materials) {
            if (material.getLeft().equals(block.getType()) && (material.getRight() == -1 || material.getRight() == (int) block.getState().getData().getData())) {
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
        materials.clear();
        materials.add(new ImmutablePair<>(material, damageValue));
    }

    @EventHandler
    public void onMonumentDestroy(ObjectiveCompleteEvent event) {
        if (event.getObjective().equals(this) && showOnScoreboard()) {
            Fireworks.spawnFireworks(region.getCenterBlock().getAlignedVector(), (region.getMax().minus(region.getMin()).length()) * 0.55 + 1, 6, MiscUtil.convertChatColorToColor(team.getColor()), 1);
            for (UUID player : playersCompleted.keySet()) {
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    Bukkit.getServer().getPluginManager().callEvent(new SnowflakeChangeEvent(Bukkit.getPlayer(player), Snowflakes.ChangeReason.MONUMENT_DESTROY, getPercentFromAmount(playersCompleted.get(player)) / 10, ChatColor.GREEN + "" + getPercentFromAmount(playersCompleted.get(player)) + ChatColor.GRAY, ChatColor.GREEN + name + ChatColor.GRAY));
                }
            }
        }
    }

    public Double getProximity() {
        return getProximityHandler().getProximity();
    }
}
