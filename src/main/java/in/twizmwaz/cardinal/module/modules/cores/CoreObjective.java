package in.twizmwaz.cardinal.module.modules.cores;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.regions.type.BlockRegion;
import in.twizmwaz.cardinal.regions.type.combinations.UnionRegion;
import in.twizmwaz.cardinal.teams.PgmTeam;
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
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoreObjective implements GameObjective {

    private final PgmTeam team;
    private final String name;
    private final String id;
    private final UnionRegion region;
    private final int leak;

    private Set<String> playersTouched;
    private Material currentType;

    private boolean touched;
    private boolean complete;

    protected CoreObjective(final PgmTeam team, final String name, final String id, final UnionRegion region, final int leak, Material type) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.leak = leak;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.region.getBlocks().contains(event.getBlock())) {
            if (event.getBlock().getType().equals(currentType)) {
                if (!GameHandler.getGameHandler().getMatch().getTeam(event.getPlayer()).equals(team)) {
                    if (!playersTouched.contains(event.getPlayer().getName())) {
                        playersTouched.add(event.getPlayer().getName());
                    }
                    this.touched = true;
                } else event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockFromTo(BlockFromToEvent event) {
        Block to = event.getToBlock();
        if (CoreObjective.getClosestCore(to.getX(), to.getY(), to.getZ()).equals(this)) {
            if (to.getType().equals(Material.LAVA) || to.getType().equals(Material.STATIONARY_LAVA)) {
                double minY = -1;
                for (Block block : region.getBlocks()) {
                    if (minY == -1 || block.getY() < minY) {
                        minY = block.getY();
                    }
                }
                if (minY - to.getY() >= leak) {
                    this.complete = true;
                    event.getToBlock().setType(Material.LAVA);
                    Bukkit.broadcastMessage(team.getCompleteName() + "'s " + ChatColor.DARK_AQUA + name + ChatColor.RED + " has leaked!");
                }
            }
        }
    }

    public UnionRegion getRegion() {
        return region;
    }

    public Material getCurrentType() {
        return currentType;
    }

    public static CoreObjective getClosestCore(double x, double y, double z) {
        CoreObjective core = null;
        double closestDistance = -1;
        for (Module module : GameHandler.getGameHandler().getModuleHandler().getModules()) {
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
