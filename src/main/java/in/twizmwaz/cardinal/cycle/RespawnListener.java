package in.twizmwaz.cardinal.cycle;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by kevin on 11/2/14.
 * This class is a temporary class until modules are implemented.
 */
public class RespawnListener implements Listener {

    private final Cardinal plugin;

    public RespawnListener(Cardinal plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(new Location(GameHandler.getGameHandler().getMatchWorld(), 0, 64, 0));

    }

}
