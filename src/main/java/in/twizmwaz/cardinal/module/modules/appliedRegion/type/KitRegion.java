package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.kit.Kit;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class KitRegion extends AppliedRegion {
    
    private final Kit kit;
    
    public KitRegion(RegionModule region, FilterModule filter, String message, Kit kit) {
        super(region, filter, message);
        this.kit = kit;
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled()) return;
        if (filter != null) {
            if (region.contains(new BlockRegion(null, event.getTo().toVector())) && !region.contains(new BlockRegion(null, event.getFrom().toVector()))
                    && !filter.evaluate(event.getTo()).equals(FilterState.DENY)) {
                kit.apply(event.getPlayer());
            }
        } else if (region.contains(new BlockRegion(null, event.getTo().toVector())) && !region.contains(new BlockRegion(null, event.getFrom().toVector()))) {
            kit.apply(event.getPlayer());
        }
        
    }
}
