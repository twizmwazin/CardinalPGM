package in.twizmwaz.cardinal.module.modules.appliedRegion.type;

import in.twizmwaz.cardinal.module.modules.appliedRegion.AppliedRegion;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.regions.RegionModule;
import in.twizmwaz.cardinal.module.modules.regions.type.BlockRegion;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerAttackEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashSet;
import java.util.Set;

public class BlockEventRegion extends AppliedRegion {

    public BlockEventRegion(RegionModule region, FilterModule filter, String message) {
        super(region, filter, message);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.isCancelled() && filter.evaluate(event.getPlayer(), event.getBlock()).equals(FilterState.DENY)
                && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.isCancelled() && filter.evaluate(event.getPlayer(), event.getBlockPlaced(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
        if ((event.getBlock().getType().equals(Material.ACACIA_DOOR) || event.getBlock().getType().equals(Material.BIRCH_DOOR) || event.getBlock().getType().equals(Material.DARK_OAK_DOOR) || event.getBlock().getType().equals(Material.IRON_DOOR) || event.getBlock().getType().equals(Material.JUNGLE_DOOR) || event.getBlock().getType().equals(Material.SPRUCE_DOOR) || event.getBlock().getType().equals(Material.WOOD_DOOR) || event.getBlock().getType().equals(Material.WOODEN_DOOR) || event.getBlock().getType().equals(Material.LONG_GRASS)) && region.contains(new BlockRegion(null, event.getBlock().getLocation().add(0, 1, 0).toVector())) && !event.isCancelled() && filter.evaluate(event.getPlayer(), event.getBlockPlaced(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Material newMaterial = (event.getBucket().equals(Material.WATER_BUCKET) ? Material.WATER : (event.getBucket().equals(Material.LAVA_BUCKET) ? Material.LAVA : Material.AIR));
        if (!event.isCancelled() && filter.evaluate(event.getPlayer(), newMaterial, event).equals(FilterState.DENY)
                && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector()))) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.isCancelled() && filter.evaluate(event.getPlayer(), event.getBlockClicked().getRelative(event.getBlockFace()), event).equals(FilterState.DENY)
                && region.contains(new BlockRegion(null, event.getBlockClicked().getRelative(event.getBlockFace()).getLocation().toVector()))) {
            event.setCancelled(true);
            ChatUtil.sendWarningMessage(event.getPlayer(), message);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (filter.evaluate(event.getToBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getToBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPiston(BlockPistonExtendEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPiston(BlockPistonRetractEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (filter.evaluate(event.getBlock(), event).equals(FilterState.DENY) && region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector()))) {
            event.setCancelled(true);
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
    public void onItemFrameRotate(PlayerInteractEntityEvent event) {
        if (region.contains(event.getRightClicked().getLocation().toVector()) && event.getRightClicked() instanceof ItemFrame && filter.evaluate(event.getPlayer(), event.getRightClicked(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
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

    @EventHandler
    public void onPlayerAttackEntity(PlayerAttackEntityEvent event) {
        if (event.getLeftClicked() instanceof ItemFrame) {
            if (region.contains(event.getLeftClicked().getLocation().toVector()) && filter.evaluate(event.getLeftClicked(), event.getPlayer(), event).equals(FilterState.DENY)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (region.contains(event.getEntity().getLocation().toVector()) && filter.evaluate(event.getEntity(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (region.contains(block.getRelative(event.getDirection()).getLocation().toVector()) && filter.evaluate(block, event).equals(FilterState.DENY)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        if (region.contains(new BlockRegion(null, event.getBlock().getLocation().toVector())) && filter.evaluate(event.getBlock(), event).equals(FilterState.DENY)) {
            event.setCancelled(true);
        }
    }
}

