package in.twizmwaz.cardinal.module.modules.visibility;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.PlayerVisibilityChangeEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.settings.Settings;
import in.twizmwaz.cardinal.util.Teams;
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

    private void resetVisibility(Player viewer, Player toSee, Optional<TeamModule> newTeam) {
        try {
            boolean showObs = Settings.getSettingByName("Observers") == null || !Settings.getSettingByName("Observers").getValueByPlayer(viewer).getValue().equalsIgnoreCase("none");
            if (match.getState().equals(MatchState.PLAYING)) {
                if (ObserverModule.testDead(toSee)) {
                    setVisibility(viewer, toSee, false);
                } else if (ObserverModule.testObserver(viewer)) {
                    setVisibility(viewer, toSee, !(newTeam.isPresent() && newTeam.get().isObserver() && !showObs));
                } else {
                    setVisibility(viewer, toSee, !(newTeam.isPresent() && newTeam.get().isObserver()));
                }
            } else {
                setVisibility(viewer, toSee, showObs);
            }
        } catch (NullPointerException e) {
            viewer.showPlayer(toSee);
        }
    }

    private void setVisibility(final Player viewer, final Player toSee, boolean shouldSee) {
        if (shouldSee) {
            viewer.showPlayer(toSee);
        } else {
            viewer.hidePlayer(toSee);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            this.resetVisibility(viewer, event.getPlayer(), Teams.getTeamByPlayer(event.getPlayer()));
            this.resetVisibility(event.getPlayer(), viewer, Teams.getTeamByPlayer(viewer));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMatchStart(MatchStartEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player toSee : Bukkit.getOnlinePlayers()) {
                this.resetVisibility(viewer, toSee, Teams.getTeamByPlayer(toSee));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(CardinalSpawnEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Cardinal.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player viewer : Bukkit.getOnlinePlayers()) {
                    resetVisibility(viewer, player, Teams.getTeamByPlayer(player));
                }
            }
        }, 5L);

    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player toSee : Bukkit.getOnlinePlayers()) {
                this.resetVisibility(viewer, toSee, Teams.getTeamByPlayer(toSee));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        Player switched = event.getPlayer();
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            this.resetVisibility(viewer, switched, event.getNewTeam());
            this.resetVisibility(switched, viewer, Teams.getTeamByPlayer(viewer));
        }
    }

    @EventHandler
    public void onPlayerVisibilityChange(PlayerVisibilityChangeEvent event) {
        for (Player toSee : Bukkit.getOnlinePlayers()) {
            this.resetVisibility(event.getPlayer(), toSee, Teams.getTeamByPlayer(toSee));
        }
    }
}
