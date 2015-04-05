package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class VelocityRegion extends AppliedRegion {
    
    public final Vector velocity;
    
    public VelocityRegion(RegionModule region, FilterModule filter, String message, Vector velocity) {
        super(region, filter, message);
        this.velocity = velocity;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (region.contains(event.getTo().toVector()) && (filter == null || filter.evaluate(event.getPlayer(), event).equals(FilterState.ALLOW))) {
            event.getPlayer().setVelocity(velocity);
        }
    }
}
