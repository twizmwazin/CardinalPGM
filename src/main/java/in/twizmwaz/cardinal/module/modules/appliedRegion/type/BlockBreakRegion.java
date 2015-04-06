package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import java.util.HashSet;
import java.util.Set;

public class BlockBreakRegion extends AppliedRegion {
    
    public BlockBreakRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getBlock(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector())) && filter.evaluate(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Set<Block> blocksToRemove = new HashSet<>();
        for (Block block : event.blockList()) {
            if (region.contains(new BlockRegion(null, block.getLocation().toVector()))) {
                if (filter.evaluate(block, event).equals(FilterState.DENY)) {
                    blocksToRemove.add(block);
                }
            }
        }
        for (Block block : blocksToRemove) {
            event.blockList().remove(block);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (event instanceof HangingBreakByEntityEvent) {
            if (region.contains(event.getEntity().getLocation().toVector()) && filter.evaluate(event.getEntity(), ((HangingBreakByEntityEvent) event).getRemover(), event).equals(FilterState.DENY)) {
                event.setCancelled(true);
            }
        } else {
            if (region.contains(event.getEntity().getLocation().toVector()) && filter.evaluate(event.getEntity(), event).equals(FilterState.DENY)) {
                event.setCancelled(true);
            }
        }
    }
}
