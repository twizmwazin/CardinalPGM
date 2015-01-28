package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakRegion extends AppliedRegion {
    
    private final FilterModule filter;
    private final String message;
    
    public BlockBreakRegion(RegionModule region, FilterModule filter, String message) {
        super(region);
        this.filter = filter;
        this.message = message;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (getRegion().contains(new BlockRegion(null, event.getBlock().getLocation().toVector())) &&
                (filter.evaluate(event.getPlayer()) == FilterState.DENY || filter.evaluate(event.getBlock()) == FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
            
    }
}
