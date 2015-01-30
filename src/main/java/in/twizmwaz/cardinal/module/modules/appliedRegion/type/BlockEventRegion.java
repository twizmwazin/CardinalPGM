package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.util.Vector;

public class BlockEventRegion extends AppliedRegion {

    public BlockEventRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if ((filter.evaluate(event.getBlock()).equals(FilterState.DENY) || filter.evaluate(event.getPlayer()).equals(FilterState.DENY))
                && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if ((filter.evaluate(event.getBlock()).equals(FilterState.DENY) || filter.evaluate(event.getPlayer()).equals(FilterState.DENY))
                && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if ((filter.evaluate(event.getBlockClicked().getRelative(event.getBlockFace())).equals(FilterState.DENY) || filter.evaluate(event.getPlayer()).equals(FilterState.DENY))
                && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler

    public void onBucketFill(PlayerBucketFillEvent event) {
        if ((filter.evaluate(event.getBlockClicked().getRelative(event.getBlockFace())).equals(FilterState.DENY) || filter.evaluate(event.getPlayer()).equals(FilterState.DENY))
                && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (filter.evaluate(event.getToBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getToBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPiston(BlockPistonExtendEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPiston(BlockPistonRetractEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (filter.evaluate(event.getBlock()).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector().add(new Vector(0.5, 0.5, 0.5))))) {
            event.setCancelled(true);
        }
    }
}

