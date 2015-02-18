package in.twizmwaz.cardinal.module.modules.matchTimer;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class MatchTimer implements Module {

    private long startTime;
    private double endTime;

    protected MatchTimer() {
        this.endTime = 0;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
    
    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.startTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onMatchEnd(MatchEndEvent event) {
        this.endTime = ((double) System.currentTimeMillis() - (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class)).getTime()) / 1000.0;
    }

    /**
     * @return The current time stored in the module.
     */
    public long getTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public static double getTimeInSeconds() {
        Match match = GameHandler.getGameHandler().getMatch();
        if (match.isRunning()) {
            return ((double) System.currentTimeMillis() - (GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class)).getTime()) / 1000.0;
        }
        if (match.getState().equals(MatchState.ENDED) || match.getState().equals(MatchState.CYCLING)) {
            return GameHandler.getGameHandler().getMatch().getModules().getModule(MatchTimer.class).getEndTime();
        }
        return 0;
    }
}
