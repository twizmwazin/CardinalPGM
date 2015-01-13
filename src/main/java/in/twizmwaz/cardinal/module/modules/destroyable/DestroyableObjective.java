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
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class DestroyableObjective implements GameObjective {

    private final PgmTeam team;
    private final String name;
    private final String id;
    private final Region region;
    private final Material type;
    private final int damageValue;
    private final double required;

    private double complete;
    private boolean completed;
    private boolean showPercent;
    private boolean repairable;

    protected DestroyableObjective(final PgmTeam team, final String name, final String id, final Region region, final Material type, final int damageValue, final double required, boolean showPercent, boolean repairable) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.region = region;
        this.type = type;
        this.damageValue = damageValue;
        this.showPercent = showPercent;
        this.repairable = repairable;
        this.complete = 0;
        this.required = required;
        this.completed = false;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!GameHandler.getGameHandler().getMatch().getTeam(event.getPlayer()).equals(team)) {
            if (this.region.getBlocks().contains(event.getBlock()))
                this.complete = this.complete + (1 / this.getMonumentSize());
            if (this.complete >= this.required) {
                this.completed = true;
                Bukkit.broadcastMessage(team.getCompleteName() + "'s " + ChatColor.AQUA + name + ChatColor.GRAY + " destroyed by " + ChatColor.DARK_AQUA + "the enemy");
                ObjectiveCompleteEvent compEvent = new ObjectiveCompleteEvent(this);
                Bukkit.getServer().getPluginManager().callEvent(compEvent);
                event.setCancelled(false);
            }
        } else event.setCancelled(true);
    }

    public double getMonumentSize() {
        double size = 0.0;
        for (Block block : region.getBlocks()) {
            if (block.getType().equals(type) && block.getData() == damageValue) {
                size ++;
            }
        }
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
}
