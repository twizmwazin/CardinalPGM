package in.twizmwaz.cardinal.module.modules.shutdown;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.event.MatchEndEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.cycleTimer.CycleTimerModule;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class ShutdownModule implements Module {

    private static int ended = 1;
    private Match match;

    public ShutdownModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void matchEnd(MatchEndEvent e) {
        ended++;
        if (Cardinal.getInstance().getConfig().getInt("shutdownAfter") <= 0) {
            return;
        }

        if (ShutdownModule.ended > Cardinal.getInstance().getConfig().getInt("shutdownAfter")) {
            match.getModules().getModule(CycleTimerModule.class).setShuttingDown(true);
        }
    }

    public static int getEnded() {
        return ended;
    }

}
