package in.twizmwaz.cardinal.module.modules.destroyable;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class Destroyable implements GameObjective {
    
    private final PgmTeam team;
    private final String name;
    private final String id;
    
    private List<Block> blocks;
    private double complete;
    private double required;
    private boolean completed;
    
    protected Destroyable(final PgmTeam team, final String name, final String id, final List<Block> blocks, double required) {
        this.team = team;
        this.name = name;
        this.id = id;
        this.blocks = blocks;
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
        if (this.blocks.contains(event.getBlock())) this.complete = this.complete + (1 / this.blocks.size());
        if (this.complete >= this.required) this.completed = true;
    }
    
}
