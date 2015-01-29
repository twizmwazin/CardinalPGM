package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceRegion extends AppliedRegion {
    
    private final FilterModule filter;
    private final String message;
    
    public BlockPlaceRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
        this.filter = filter;
        this.message = message;
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (region.contains(new BlockRegion(null, event.getBlockPlaced().getLocation().toVector())) && (filter.evaluate(event.getPlayer()).equals(FilterState.DENY) || filter.evaluate(event.getBlockPlaced()).equals(FilterState.DENY))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
