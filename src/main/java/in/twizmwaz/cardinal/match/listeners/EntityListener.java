package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityListener implements Listener {

    private final JavaPlugin plugin;
    private final Match match;

    public EntityListener(JavaPlugin plugin, Match match) {
        this.plugin = plugin;
        this.match = match;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (GameHandler.getGameHandler().getMatch().getState() != MatchState.PLAYING) {
            event.setCancelled(true);
        }
    }

}
