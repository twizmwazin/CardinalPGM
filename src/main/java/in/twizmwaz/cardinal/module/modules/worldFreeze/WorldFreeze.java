package in.twizmwaz.cardinal.module.modules.worldFreeze;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class WorldFreeze implements Module {
    
    private final Match match;
    
    protected WorldFreeze(Match match) {
        this.match = match;
    }
    
    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        if (match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (match.getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }
    
}
