package in.twizmwaz.cardinal.module.modules.buildHeight;

import in.twizmwaz.cardinal.module.Module;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jdom2.Document;

public class BuildHeight extends Module {

    private final JavaPlugin plugin;
    private final int height;

    public BuildHeight(Document doc) {
        this.plugin = super.plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        height = Integer.parseInt(doc.getRootElement().getChild("maxbuildheight").getValue());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getY() >= height) {
            event.setCancelled(true);
        }
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getY() >= height) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEmptyBucket(PlayerBucketEmptyEvent event) {
        Block toFill = event.getBlockClicked().getRelative(event.getBlockFace());
        if (toFill.getY() >= height) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFillBucket(PlayerBucketFillEvent event) {
        Block toEmpty = event.getBlockClicked().getRelative(event.getBlockFace());
        if (toEmpty.getY() >= height) {
            event.setCancelled(true);
        }
    }


}
