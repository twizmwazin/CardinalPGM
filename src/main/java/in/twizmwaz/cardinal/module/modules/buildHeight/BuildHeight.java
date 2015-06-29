package in.twizmwaz.cardinal.module.modules.buildHeight;

import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.HashSet;
import java.util.Set;

public class BuildHeight implements Module {

    private final int height;

    protected BuildHeight(int height) {
        this.height = height;

    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtils.sendWarningMessage(event.getPlayer(), "You have reached the maximum build height! " + ChatColor.GRAY + "(" + ChatColor.AQUA + height + ChatColor.GRAY + " blocks)");
        }
        if ((event.getBlock().getType().equals(Material.ACACIA_DOOR) || event.getBlock().getType().equals(Material.BIRCH_DOOR) || event.getBlock().getType().equals(Material.DARK_OAK_DOOR) || event.getBlock().getType().equals(Material.IRON_DOOR) || event.getBlock().getType().equals(Material.JUNGLE_DOOR) || event.getBlock().getType().equals(Material.SPRUCE_DOOR) || event.getBlock().getType().equals(Material.WOOD_DOOR) || event.getBlock().getType().equals(Material.WOODEN_DOOR) || event.getBlock().getType().equals(Material.LONG_GRASS)) && event.getBlock().getY() + 1 >= height) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            ChatUtils.sendWarningMessage(event.getPlayer(), "You have reached the maximum build height! " + ChatColor.GRAY + "(" + ChatColor.AQUA + height + ChatColor.GRAY + " blocks)");
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), "You have reached the maximum build height! " + ChatColor.GRAY + "(" + ChatColor.AQUA + height + ChatColor.GRAY + " blocks)");
        }
    }

    @EventHandler
    public void onEmptyBucket(PlayerBucketEmptyEvent event) {
        Block toFill = event.getBlockClicked().getRelative(event.getBlockFace());
        if (toFill.getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), "You have reached the maximum build height! " + ChatColor.GRAY + "(" + ChatColor.AQUA + height + ChatColor.GRAY + " blocks)");
        }
    }

    @EventHandler
    public void onFillBucket(PlayerBucketFillEvent event) {
        Block toEmpty = event.getBlockClicked().getRelative(event.getBlockFace());
        if (toEmpty.getY() >= height && !event.isCancelled()) {
            event.setCancelled(true);
            ChatUtils.sendWarningMessage(event.getPlayer(), "You have reached the maximum build height! " + ChatColor.GRAY + "(" + ChatColor.AQUA + height + ChatColor.GRAY + " blocks)");
        }
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        Set<BlockState> blocksAboveHeight = new HashSet<>();
        for (BlockState blockState : event.getBlocks()) {
            if (blockState.getY() >= height) {
                blocksAboveHeight.add(blockState);
            }
        }
        for (BlockState blockState : blocksAboveHeight) {
            event.getBlocks().remove(blockState);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (block.getRelative(event.getDirection()).getY() >= height) {
                event.setCancelled(true);
            }
        }
    }
}
