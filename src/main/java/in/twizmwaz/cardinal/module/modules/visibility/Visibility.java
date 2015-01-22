package in.twizmwaz.cardinal.module.modules.visibility;

import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    private void resetVisibility(Player viewer, Player toSee, TeamModule newTeam) {
        try {
            if (match.getState().equals(MatchState.PLAYING)) {
                if (TeamUtils.getTeamByPlayer(viewer).isObserver()) {
                    viewer.showPlayer(toSee);
                } else if (newTeam.isObserver()) {
                    viewer.hidePlayer(toSee);
                } else {
                    viewer.showPlayer(toSee);
                }
            } else viewer.showPlayer(toSee);
        } catch (NullPointerException e) {
            viewer.showPlayer(toSee);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) this.resetVisibility(viewer, event.getPlayer(), TeamUtils.getTeamByPlayer(event.getPlayer()));
        for (Player online : Bukkit.getOnlinePlayers()) this.resetVisibility(event.getPlayer(), online, TeamUtils.getTeamByPlayer(online));
    }

    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player toSee : Bukkit.getOnlinePlayers()) {
                this.resetVisibility(viewer, toSee, TeamUtils.getTeamByPlayer(toSee));
            }
        }
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player toSee : Bukkit.getOnlinePlayers()) {
                this.resetVisibility(viewer, toSee, TeamUtils.getTeamByPlayer(toSee));
            }
        }
    }

    @EventHandler
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        Player switched = event.getPlayer();
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            this.resetVisibility(viewer, switched, event.getNewTeam());
        }
    }
}
