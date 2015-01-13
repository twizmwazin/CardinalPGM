package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DestroyableObjective implements GameObjective {

    private final PgmTeam team;
    private final String name;
    private final String id;
    private final Region region;
    private final List<Material> types;
    private final List<Integer> damageValues;
    private final double required;

    private Set<String> playersTouched;
    private double size;

    private double complete;
    private boolean completed;
    private boolean showPercent;
    private boolean repairable;

    protected DestroyableObjective(final PgmTeam team, final String name, final String id, final Region region, final List<Material> types, final List<Integer> damageValues, final double required, boolean showPercent, boolean repairable) {
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
        this.completed = false;

        this.playersTouched = new HashSet<>();

        size = 0.0;
        for (Block block : region.getBlocks()) {
            for (int i = 0; i < types.size(); i ++) {
                if (types.get(i).equals(block.getType()) && damageValues.get(i) == (int) block.getState().getData().getData()) {
                    size ++;
                    break;
                }
            }
        }
    }

    @Override
    public PgmTeam getTeam() {
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
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (getBlocks().contains(event.getBlock())) {
            if (!GameHandler.getGameHandler().getMatch().getTeam(event.getPlayer()).equals(team)) {
                this.complete += (1 / size);
                if (this.complete >= this.required && !this.completed) {
                    this.completed = true;
                    event.setCancelled(false);
                    Bukkit.broadcastMessage(team.getCompleteName() + "'s " + ChatColor.AQUA + name + ChatColor.GRAY + " destroyed by " + ChatColor.DARK_AQUA + "the enemy");
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this);
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
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
        for (Block block : objectiveBlownUp) {
            boolean blockDestroyed = false;
            if (event.getEntity().hasMetadata("source")) {
                String player = event.getEntity().getMetadata("source").get(0).asString();
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (GameHandler.getGameHandler().getMatch().getTeam(Bukkit.getPlayer(player)).equals(team)) {
                        event.blockList().remove(block);
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
                        }
                        blockDestroyed = true;
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
                    ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this);
                    Bukkit.getServer().getPluginManager().callEvent(compEvent);
                }
            }
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
