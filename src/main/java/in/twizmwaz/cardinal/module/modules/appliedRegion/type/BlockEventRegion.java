package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class BlockEventRegion extends AppliedRegion {
    
    public BlockEventRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBurnBreak(BlockBurnEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (filter.evaluate(event.getToBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getToBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPiston(BlockPistonEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }
}
