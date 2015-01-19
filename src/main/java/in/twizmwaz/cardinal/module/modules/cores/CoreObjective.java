package in.twizmwaz.cardinal.module.modules.cores;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.TeamChat;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
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
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class CoreObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final RegionModule region;
    private final int leak;
    private final int damageValue;
    private boolean show;

    private Set<UUID> playersTouched;
    private Material currentType;

    private boolean touched;
    private boolean complete;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected CoreObjective(final TeamModule team, final String name, final String id, final RegionModule region, final int leak, final Material type, final int damageValue, final boolean show) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.leak = leak;
        this.damageValue = damageValue;
        this.show = show;

        this.playersTouched = new HashSet<>();
        this.currentType = type;

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
    public GameObjectiveScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (getBlocks().contains(event.getBlock())) {
            if (TeamUtils.getTeamByPlayer(event.getPlayer()) != team) {
                if (!playersTouched.contains(event.getPlayer().getUniqueId())) {
                    playersTouched.add(event.getPlayer().getUniqueId());
                    TeamModule teamModule = TeamUtils.getTeamByPlayer(event.getPlayer());
                    TeamChat.sendToTeam(teamModule.getColor() + "[Team] " + event.getPlayer().getDisplayName() + ChatColor.GRAY + " broke a piece of " + ChatColor.RED + name, teamModule);
                }
                boolean oldState = this.touched;
                this.touched = true;
                ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), !oldState);
                Bukkit.getServer().getPluginManager().callEvent(touchEvent);
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
                ChatUtils.sendWarningMessage(event.getPlayer(), "You may not damage your own core.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> objectiveBlownUp = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (getBlocks().contains(block)) {
                objectiveBlownUp.add(block);
            }
        }
        boolean oldState = this.touched;
        boolean blownUp = false;
        Player eventPlayer = null;
        for (Block block : objectiveBlownUp) {
            if (TntTracker.getWhoPlaced(event.getEntity()) != null) {
                UUID player = TntTracker.getWhoPlaced(event.getEntity());
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)) == team) {
                        event.blockList().remove(block);
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
                            TeamModule teamModule = TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player));
                            TeamChat.sendToTeam(teamModule.getColor() + "[Team] " + Bukkit.getPlayer(player).getDisplayName() + ChatColor.GRAY + " broke a piece of " + ChatColor.RED + name, teamModule);
                        }
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
            ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, !oldState);
            Bukkit.getServer().getPluginManager().callEvent(touchEvent);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(BlockFromToEvent event) {
        Block to = event.getToBlock();
        Block from = event.getBlock();
        if (CoreObjective.getClosestCore(to.getX(), to.getY(), to.getZ()).equals(this)) {
            if ((from.getType().equals(Material.LAVA) || from.getType().equals(Material.STATIONARY_LAVA)) && to.getType().equals(Material.AIR)) {
                double minY = 256;
                for (Block block : getBlocks()) {
                    if (block.getY() < minY) minY = block.getY();
                }
                if (minY - to.getY() >= leak && !this.complete) {
                    this.complete = true;
                    event.setCancelled(false);
                    Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.RED + "'s " + ChatColor.DARK_AQUA + name + ChatColor.RED + " has leaked!");
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, null);
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
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

    public boolean partOfObjective(Block block) {
        return currentType.equals(block.getType()) && damageValue == (int) block.getState().getData().getData();
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

    public static CoreObjective getClosestCore(double x, double y, double z) {
        CoreObjective core = null;
        double closestDistance = -1;
        for (Module module : GameHandler.getGameHandler().getMatch().getModules()) {
            if (module instanceof CoreObjective) {
                BlockRegion center = ((CoreObjective) module).getRegion().getCenterBlock();
                if (closestDistance == -1 || new Vector(x, y, z).distance(new Vector(center.getX(), center.getY(), center.getZ())) < closestDistance) {
                    core = (CoreObjective) module;
                    closestDistance = new Vector(x, y, z).distance(new Vector(center.getX(), center.getY(), center.getZ()));
                }
            }
        }
        return core;
    }
}
