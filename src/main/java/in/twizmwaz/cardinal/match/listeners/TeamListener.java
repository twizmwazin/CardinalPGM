package in.twizmwaz.cardinal.match.listeners;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
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
            try {
                if (event.getOldTeam().isObserver()) {
                    PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().teleport(event.getNewTeam().getSpawnPoint());
                    }
                } else if (event.getNewTeam().isObserver()) {
                    PgmSpawnEvent spawnEvent = new PgmSpawnEvent(event.getPlayer());
                    Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                    if (!spawnEvent.isCancelled()) {
                        event.getPlayer().setGameMode(GameMode.CREATIVE);
                    }
                } else {
                    event.getPlayer().setHealth(0);
                }
            } catch (NullPointerException ex) {

            }
        }
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!match.getTeam(player).isObserver()) {
                PgmSpawnEvent spawnEvent = new PgmSpawnEvent(player);
                Bukkit.getServer().getPluginManager().callEvent(spawnEvent);
                if (!spawnEvent.isCancelled()) {
                    player.teleport(match.getTeam(player).getSpawnPoint());
                }
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onPlayerSpawn(PgmSpawnEvent event) {
        event.getPlayer().setGameMode(GameMode.SURVIVAL);
        event.getPlayer().setHealth(20);
        event.getPlayer().setFoodLevel(20);
        event.getPlayer().setSaturation(20);
    }


}
