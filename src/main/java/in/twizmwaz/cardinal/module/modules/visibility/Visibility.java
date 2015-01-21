package in.twizmwaz.cardinal.module.modules.visibility;

import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;

public class Visibility implements Module {

    private final Match match;

    protected Visibility(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                player.showPlayer(otherPlayer);
            }
        }
    }

    private void resetVisibility(Player viewer, Player toSee) {
        try {
            if (match.getState().equals(MatchState.PLAYING)) {
                if (TeamUtils.getTeamByPlayer(viewer).isObserver()) {
                    viewer.showPlayer(toSee);
                } else if (TeamUtils.getTeamByPlayer(toSee).isObserver()) {
                    viewer.hidePlayer(toSee);
                } else {
                    viewer.showPlayer(toSee);
                }
            } else viewer.showPlayer(toSee);
        } catch (NullPointerException e) {
            viewer.showPlayer(toSee);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) this.resetVisibility(viewer, event.getPlayer());
        for (Player online : Bukkit.getOnlinePlayers()) this.resetVisibility(event.getPlayer(), online);

    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player toSee : Bukkit.getOnlinePlayers()) {
                this.resetVisibility(viewer, toSee);
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player toSee : Bukkit.getOnlinePlayers()) {
                this.resetVisibility(viewer, toSee);
            }
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        Player switched = event.getPlayer();
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other.equals(event.getPlayer())) break;
            if (match.getState().equals(MatchState.PLAYING)) {
                if (TeamUtils.getTeamByPlayer(other).isObserver() && !event.getNewTeam().isObserver()) {
                    switched.hidePlayer(other);
                    other.showPlayer(switched);
                } else if (event.getNewTeam().isObserver() && !TeamUtils.getTeamByPlayer(other).isObserver()) {
                    switched.showPlayer(other);
                    other.hidePlayer(switched);
                } else {
                    switched.showPlayer(other);
                    other.showPlayer(switched);
                }
            } else {
                switched.showPlayer(other);
                other.showPlayer(switched);
            }
        }
    }
}
