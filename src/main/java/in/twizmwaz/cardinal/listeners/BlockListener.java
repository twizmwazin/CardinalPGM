package in.twizmwaz.cardinal.listeners;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class BlockListener implements Listener {

    private final Cardinal plugin;

    public BlockListener(Cardinal plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        if (GameHandler.getGameHandler().getMatch().getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

}
