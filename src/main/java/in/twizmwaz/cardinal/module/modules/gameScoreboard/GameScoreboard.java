package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveTouchEvent;
import in.twizmwaz.cardinal.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class GameScoreboard implements Module {

    protected GameScoreboard() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onObjectiveTouch(ObjectiveTouchEvent event) {
        if (event.updateScoreboard()) {
            updateScoreboard();
        }
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        updateScoreboard();
    }

    public void updateScoreboard() {

    }
}
