package in.twizmwaz.cardinal.module.modules.fallingBlocks;

import com.google.common.collect.Lists;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public class FallingBlocksModule implements Module {

    private static BlockFace[] FACES = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    private Set<Rule> rules;

    public FallingBlocksModule(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        scheduleUpdate(event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        scheduleUpdate(event.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockUpdate(BlockPhysicsEvent event) {
        scheduleUpdate(event.getBlock());
    }

    private void scheduleUpdate(final Block block) {
        for (final Rule rule : rules) {
            Bukkit.getScheduler().runTaskLater(Cardinal.getInstance(), new Runnable() {
                @Override
                public void run() {
                    updateBlock(block, rule);
                }
            }, rule.getDelay());
        }
    }

    private void updateBlock(Block block, Rule rule) {
        if(rule.getFilter().evaluate(block).equals(FilterState.ALLOW)) {
            if (!isBlockAttached(block, rule)) {
                if (block.getRelative(BlockFace.DOWN).getType().equals(Material.AIR)) {
                    Material material = block.getType();
                    byte data = block.getData();
                    block.setType(Material.AIR);
                    Cardinal.getInstance().getGameHandler().getMatchWorld().spawnFallingBlock(block.getLocation(), material, data);
                } else {
                    updateBlock(block.getRelative(BlockFace.DOWN), rule);
                }
            }
        }
    }

    private boolean isBlockAttached(Block source, Rule rule) {
        List<Block> nextScan = Lists.newArrayList();
        List<Vector> alreadyScanned = Lists.newArrayList();
        nextScan.add(source);
        alreadyScanned.add(source.getLocation().toVector());
        return isBlockAttached(nextScan, rule, alreadyScanned);
    }

    private boolean isBlockAttached(List<Block> scan, Rule rule, List<Vector> alreadyScanned) {
        if (scan.size() == 0) return false;
        List<Block> nextScan = Lists.newArrayList();
        for (Block scanning : scan) {
            if (alreadyScanned.size() > 4096) {
                Bukkit.getConsoleSender().sendMessage("Maximim scan area (4096 blocks) was reached.");
                return true;
            }
            for (BlockFace face : FACES) {
                Block block = scanning.getRelative(face);
                if (alreadyScanned.contains(block.getLocation().toVector())) continue;
                if (rule.getSticky().evaluate(block).equals(FilterState.ALLOW) || (face.equals(BlockFace.DOWN) && !block.getType().equals(Material.AIR))) {
                    if (rule.getFilter().evaluate(block).equals(FilterState.ALLOW)) {
                        nextScan.add(block);
                        alreadyScanned.add(block.getLocation().toVector());
                    } else return true;
                }
            }
        }
        return isBlockAttached(nextScan, rule, alreadyScanned);
    }

}
