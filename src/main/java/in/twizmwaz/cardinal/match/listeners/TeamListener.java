package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by kevin on 11/24/14.
 */
public class TeamListener implements Listener {

    private final JavaPlugin plugin;
    private final Match match;

    public TeamListener(JavaPlugin plugin, Match match) {
        this.plugin = plugin;
        this.match = match;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTeamChange(PlayerChangeTeamEvent event) {
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.PLAYING)) {
            if (event.getOldTeam().isObserver()) {
                event.getPlayer().teleport(event.getNewTeam().getSpawnPoint());
            } else {
                event.getPlayer().setHealth(0);
            }
        }
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.CYCLING)) {
            event.setCancelled(true);
        }
    }


}
