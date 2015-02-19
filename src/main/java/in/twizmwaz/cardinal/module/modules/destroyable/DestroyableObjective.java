package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.ScoreboardUpdateEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.FireworkUtil;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class DestroyableObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final RegionModule region;
    private List<Material> types;
    private List<Integer> damageValues;
    private final double required;
    private final boolean showPercent;
    private final boolean repairable;
    private final boolean show;
    private boolean changesModes;

    private double proximity;

    private Set<UUID> playersTouched;
    private double size;
    private HashMap<UUID, Integer> playerDestroyed;
    private List<Block> monument;

    private double complete;
    private boolean completed;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected DestroyableObjective(final TeamModule team, final String name, final String id, final RegionModule region, List<Material> types, List<Integer> damageValues, final double required, final boolean show, boolean changesModes, boolean showPercent, boolean repairable) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.types = types;
        this.damageValues = damageValues;
        this.showPercent = showPercent;
        this.repairable = repairable;
        this.complete = 0;
        this.required = required;
        this.show = show;
        this.changesModes = changesModes;
        this.completed = false;

        this.proximity = Double.POSITIVE_INFINITY;

        this.playersTouched = new HashSet<>();
        this.playerDestroyed = new HashMap<>();

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
                if (TeamUtils.getTeamByPlayer(event.getPlayer()) != team) {
                    if (!playersTouched.contains(event.getPlayer().getUniqueId())) {
                        playersTouched.add(event.getPlayer().getUniqueId());
                        TeamModule teamModule = TeamUtils.getTeamByPlayer(event.getPlayer());
                        if (this.show && !this.completed) TeamUtils.getTeamChannel(teamModule).sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED, teamModule.getColor() + event.getPlayer().getDisplayName() + ChatColor.GRAY, ChatColor.AQUA + name));
                    }
                    boolean oldState = this.isTouched();
                    this.complete++;
                    this.playerDestroyed.put(event.getPlayer().getUniqueId(), (playerDestroyed.containsKey(event.getPlayer().getUniqueId()) ? playerDestroyed.get(event.getPlayer().getUniqueId()) + 1 : 1));
                    if ((this.complete / size) >= this.required && !this.completed) {
                        this.completed = true;
                        event.setCancelled(false);
                        if (this.show) {
                            for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage(ChatColor.GRAY + new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, team.getCompleteName() + ChatColor.GRAY, ChatColor.AQUA + name + ChatColor.GRAY, getWhoDestroyed(player.getLocale()))).getMessage(player.getLocale()));
                        }
                        FireworkUtil.spawnFirework(event.getPlayer().getLocation(), event.getPlayer().getWorld());
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                    } else if (!this.completed) {
                        ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), !oldState || showPercent);
                        Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                    }
                } else {
                    event.setCancelled(true);
                    if (this.show) ChatUtils.sendWarningMessage(event.getPlayer(), new LocalizedChatMessage(ChatConstant.ERROR_OWN_OBJECTIVE));
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
            for (Block block : objectiveBlownUp) {
                boolean blockDestroyed = false;
                if (TntTracker.getWhoPlaced(event.getEntity()) != null) {
                    UUID player = TntTracker.getWhoPlaced(event.getEntity());
                    if (Bukkit.getOfflinePlayer(player).isOnline()) {
                        if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)) == team) {
                            event.blockList().remove(block);
                        } else {
                            if (!playersTouched.contains(player)) {
                                playersTouched.add(player);
                                TeamModule teamModule = TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player));
                                if (this.show && !this.completed) TeamUtils.getTeamChannel(teamModule).sendLocalizedMessage(new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DAMAGED, teamModule.getColor() + Bukkit.getPlayer(player).getDisplayName() + ChatColor.GRAY, ChatColor.AQUA + name));
                            }
                            blockDestroyed = true;
                            blownUp = true;
                            eventPlayer = Bukkit.getPlayer(player);
                        }
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
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
                        this.playerDestroyed.put(eventPlayer.getUniqueId(), (playerDestroyed.containsKey(eventPlayer.getUniqueId()) ? playerDestroyed.get(eventPlayer.getUniqueId()) + 1 : 1));
                    if ((this.complete / size) >= this.required && !this.completed) {
                        this.completed = true;
                        if (this.show) {
                            for (Player player : Bukkit.getOnlinePlayers()) player.sendMessage(ChatColor.GRAY + new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, team.getCompleteName() + ChatColor.GRAY, ChatColor.AQUA + name + ChatColor.GRAY, getWhoDestroyed(player.getLocale()))).getMessage(player.getLocale()));
                            Bukkit.getLogger().info(ChatColor.GRAY + new UnlocalizedChatMessage("{0}", new LocalizedChatMessage(ChatConstant.UI_OBJECTIVE_DESTROYED, team.getCompleteName() + ChatColor.GRAY, ChatColor.AQUA + name + ChatColor.GRAY, getWhoDestroyed(Locale.getDefault().toString()))).getMessage(Locale.getDefault().toString()));
                        }
                        ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, eventPlayer);
                        Bukkit.getServer().getPluginManager().callEvent(compEvent);
                    }
                }
            }
            if (!this.completed && blownUp) {
                ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, !oldState || (getPercent() != originalPercent));
                Bukkit.getServer().getPluginManager().callEvent(touchEvent);
            }
        }
    }

    public int getBlocksRequired() {
        return (int) Math.ceil(size * required);
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

    public boolean isRepairable() {
        return repairable;
    }

    public int getPercent() {
        double blocksRequired = required * size;
        if (Math.floor((complete / blocksRequired) * 100) > 100) {
            return 100;
        }
        if (Math.floor((complete / blocksRequired) * 100) < 0) {
            return 0;
        }
        return (int) Math.floor((complete / blocksRequired) * 100);
    }

    public int getPercentFromAmount(int amount) {
        double blocksRequired = required * size;
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

    public List<Block> getMonument() {
        return monument;
    }

    public String getWhoDestroyed(String locale) {
        String whoDestroyed = "";
        List<String> toCombine = new ArrayList<>();
        for (UUID player : MiscUtils.getSortedHashMapKeyset(playerDestroyed)) {
            if (Bukkit.getOfflinePlayer(player).isOnline() && getPercentFromAmount(playerDestroyed.get(player)) > (100 / 3)) {
                toCombine.add(TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)).getColor() + Bukkit.getPlayer(player).getDisplayName() + ChatColor.GRAY + " (" + getPercentFromAmount(playerDestroyed.get(player)) + "%)");
            }
        }
        if (toCombine.size() == 0) {
            toCombine.add(ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.MISC_ENEMY).getMessage(locale));
        }
        if (toCombine.size() < playerDestroyed.keySet().size()) {
            toCombine.add(ChatColor.DARK_AQUA + new LocalizedChatMessage(ChatConstant.MISC_OTHERS).getMessage(locale));
        }
        whoDestroyed = toCombine.get(0);
        for (int i = 1; i < toCombine.size(); i ++) {
            whoDestroyed += ChatColor.GRAY + (i == toCombine.size() - 1 ? " " + new LocalizedChatMessage(ChatConstant.MISC_AND).getMessage(locale) + " " : ", ") + toCombine.get(i);
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
        if (GameHandler.getGameHandler().getMatch().isRunning() && !this.isTouched() && TeamUtils.getTeamByPlayer(event.getPlayer()) != null && !TeamUtils.getTeamByPlayer(event.getPlayer()).isObserver() && TeamUtils.getTeamByPlayer(event.getPlayer()) != this.team) {
            if (event.getPlayer().getLocation().toVector().distance(region.getCenterBlock().getVector()) < proximity) {
                proximity = event.getPlayer().getLocation().toVector().distance(region.getCenterBlock().getVector());
                Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent());
            }
        }
    }

    public double getProximity() {
        return proximity;
    }
}
