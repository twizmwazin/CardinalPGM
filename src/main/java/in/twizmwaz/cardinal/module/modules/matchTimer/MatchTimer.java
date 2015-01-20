package in.twizmwaz.cardinal.module.modules.matchTimer;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.MatchStartEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class MatchTimer implements Module {

    private long startTime;

    protected MatchTimer() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }
    
    @EventHandler
    public void onMatchStart(MatchStartEvent event) {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * @return The current time stored in the module.
     */
    public long getTime() {
        return startTime;
    }

    public static double getTimeInSeconds() {
        for (MatchTimer timer : GameHandler.getGameHandler().getMatch().getModules().getModules(MatchTimer.class)) {
            return ((double) System.currentTimeMillis() - timer.getTime()) / 1000.0;
        }
        return 0;
    }
}
