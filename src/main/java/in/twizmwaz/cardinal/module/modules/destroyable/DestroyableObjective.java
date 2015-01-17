package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.regions.Region;
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

import java.util.*;

public class DestroyableObjective implements GameObjective {

    private final TeamModule team;
    private final String name;
    private final String id;
    private final Region region;
    private final List<Material> types;
    private final List<Integer> damageValues;
    private final double required;
    private final boolean showPercent;
    private final boolean repairable;
    private final boolean show;

    private Set<UUID> playersTouched;
    private double size;

    private double complete;
    private boolean completed;

    private GameObjectiveScoreboardHandler scoreboardHandler;

    protected DestroyableObjective(final TeamModule team, final String name, final String id, final Region region, final List<Material> types, final List<Integer> damageValues, final double required, final boolean show, boolean showPercent, boolean repairable) {
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
        this.completed = false;

        this.playersTouched = new HashSet<>();

        size = 0.0;
        for (Block block : region.getBlocks()) {
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).equals(block.getType()) && damageValues.get(i) == (int) block.getState().getData().getData()) {
                    size++;
                    break;
                }
            }
        }

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
        if (getBlocks().contains(event.getBlock())) {
            if (!TeamUtils.getTeamByPlayer(event.getPlayer()).equals(team)) {
                this.complete += (1 / size);
                if (this.complete >= this.required && !this.completed) {
                    this.completed = true;
                    event.setCancelled(false);
                    Bukkit.broadcastMessage(team.getCompleteName() + "'s " + ChatColor.AQUA + name + ChatColor.GRAY + " destroyed by " + ChatColor.DARK_AQUA + "the enemy");
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, event.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
                } else if (!this.completed) {
                    boolean oldState = this.isTouched();
                    ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, event.getPlayer(), !oldState);
                }
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot damage your own monument!");
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
        boolean oldState = this.isTouched();
        Player eventPlayer = null;
        for (Block block : objectiveBlownUp) {
            boolean blockDestroyed = false;
            if (TntTracker.getWhoPlaced(event.getEntity()) != null) {
                UUID player = TntTracker.getWhoPlaced(event.getEntity());
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (TeamUtils.getTeamByPlayer(Bukkit.getPlayer(player)).equals(team)) {
                        event.blockList().remove(block);
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
                        }
                        blockDestroyed = true;
                        eventPlayer = Bukkit.getPlayer(player);
                    }
                } else {
                    if (!playersTouched.contains(player)) {
                        playersTouched.add(player);
                    }
                    blockDestroyed = true;
                }
            } else {
                blockDestroyed = true;
            }
            if (blockDestroyed) {
                this.complete += (1 / size);
                if (this.complete >= this.required && !this.completed) {
                    this.completed = true;
                    Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.GRAY + "'s " + ChatColor.AQUA + name + ChatColor.GRAY + " destroyed by " + ChatColor.DARK_AQUA + "the enemy");
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this, eventPlayer);
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
                }
            }
        }
        if (!this.completed) {
            ObjectiveTouchEvent touchEvent = new ObjectiveTouchEvent(this, eventPlayer, !oldState);
            Bukkit.getServer().getPluginManager().callEvent(touchEvent);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        while (playersTouched.contains(event.getEntity().getUniqueId())) {
            playersTouched.remove(event.getEntity().getUniqueId());
        }
    }

    public double getMonumentSize() {
        return size;
    }

    public boolean showPercent() {
        return showPercent;
    }

    public boolean isRepairable() {
        return repairable;
    }

    public int getPercent() {
        double blocksRequired = required * getMonumentSize();
        double blocksBroken = complete * getMonumentSize();
        return (int) Math.round((blocksRequired / blocksBroken) * 100);
    }

    public boolean partOfObjective(Block block) {
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).equals(block.getType()) && damageValues.get(i) == (int) block.getState().getData().getData()) {
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
}
