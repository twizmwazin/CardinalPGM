package in.twizmwaz.cardinal.module.modules.cores;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoreObjective implements GameObjective {

    private final PgmTeam team;
    private final String name;
    private final String id;
    private final Region region;
    private final int leak;
    private final int damageValue;
    private boolean show;

    private Set<String> playersTouched;
    private Material currentType;

    private boolean touched;
    private boolean complete;

    protected CoreObjective(final PgmTeam team, final String name, final String id, final Region region, final int leak, final Material type, final int damageValue, final boolean show) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.leak = leak;
        this.damageValue = damageValue;
        this.show = show;

        this.playersTouched = new HashSet<>();
        this.currentType = type;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public PgmTeam getTeam() {
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (getBlocks().contains(event.getBlock())) {
            if (!GameHandler.getGameHandler().getMatch().getTeam(event.getPlayer()).equals(team)) {
                if (!playersTouched.contains(event.getPlayer().getName())) {
                    playersTouched.add(event.getPlayer().getName());
                }
                this.touched = true;
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot leak your own core!");
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
            if (event.getEntity().hasMetadata("source")) {
                String player = event.getEntity().getMetadata("source").get(0).asString();
                if (Bukkit.getOfflinePlayer(player).isOnline()) {
                    if (GameHandler.getGameHandler().getMatch().getTeam(Bukkit.getPlayer(player)).equals(team)) {
                        event.blockList().remove(block);
                    } else {
                        if (!playersTouched.contains(player)) {
                            playersTouched.add(player);
                        }
                        this.touched = true;
                    }
                } else {
                    if (!playersTouched.contains(player)) {
                        playersTouched.add(player);
                    }
                    this.touched = true;
                }
            } else {
                this.touched = true;
            }
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

    public Region getRegion() {
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
