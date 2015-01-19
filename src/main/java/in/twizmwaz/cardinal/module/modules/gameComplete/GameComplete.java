package in.twizmwaz.cardinal.module.modules.gameComplete;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.Set;

public class GameComplete implements Module {

    protected GameComplete() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        for (TeamModule team : TeamUtils.getTeams()) {
            boolean skipTeam = false;
            for (GameObjective condition : TeamUtils.getShownObjectives(team)) {
                if (!condition.isComplete() && !condition.equals(event.getObjective())) skipTeam = true;
            }
            if (TeamUtils.getShownObjectives(team).size() == 0 || skipTeam) continue;
            GameHandler.getGameHandler().getMatch().end(team);
        }
    }

    @EventHandler
    public void onScoreUpdate(ScoreUpdateEvent event) {
        if (event.getScoreModule().getScore() >= ScoreModule.max()) {
            GameHandler.getGameHandler().getMatch().end(event.getScoreModule().getTeam());
        }
    }
}
