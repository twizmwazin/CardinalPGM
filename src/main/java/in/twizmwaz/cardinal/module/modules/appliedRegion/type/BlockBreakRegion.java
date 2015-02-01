package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.module.modules.tntTracker.TntTracker;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class BlockBreakRegion extends AppliedRegion {
    
    public BlockBreakRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5)))) && (filter.evaluate(event.getPlayer()).equals(FilterState.DENY) || filter.evaluate(event.getBlock()).equals(FilterState.DENY))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.isCancelled() && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))
                && (filter.evaluate(event.getPlayer()).equals(FilterState.DENY)
                || filter.evaluate(event.getBlockClicked().getRelative(event.getBlockFace())).equals(FilterState.DENY))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }
}
