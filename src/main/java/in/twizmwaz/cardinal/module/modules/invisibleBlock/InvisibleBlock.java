package in.twizmwaz.cardinal.module.modules.invisibleBlock;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class InvisibleBlock implements Module {

    protected InvisibleBlock() {
        Bukkit.getScheduler().runTaskAsynchronously(GameHandler.getGameHandler().getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Chunk chunk : GameHandler.getGameHandler().getMatchWorld().getLoadedChunks()) {
                    for (Block block36 : chunk.getBlocks(Material.getMaterial(36))) {
                        block36.setType(Material.AIR);
                        block36.setMetadata("block36", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
                    }
                    for (Block door : chunk.getBlocks(Material.IRON_DOOR_BLOCK)) {
                        if (door.getRelative(BlockFace.DOWN).getType() != Material.IRON_DOOR_BLOCK
                                && door.getRelative(BlockFace.UP).getType() != Material.IRON_DOOR_BLOCK)
                            door.setType(Material.BARRIER);
                    }
                }
            }
        });
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        final Chunk chunk = event.getChunk();
        Bukkit.getScheduler().runTaskAsynchronously(GameHandler.getGameHandler().getPlugin(), new Runnable() {
            @Override
            public void run() {
                for (Block block36 : chunk.getBlocks(Material.getMaterial(36))) {
                    block36.setType(Material.AIR);
                    block36.setMetadata("block36", new FixedMetadataValue(GameHandler.getGameHandler().getPlugin(), true));
                }
                for (Block door : chunk.getBlocks(Material.IRON_DOOR_BLOCK)) {
                    if (door.getRelative(BlockFace.DOWN).getType() != Material.IRON_DOOR_BLOCK
                            && door.getRelative(BlockFace.UP).getType() != Material.IRON_DOOR_BLOCK)
                        door.setType(Material.BARRIER);
                }
            }
        });
    }

}
