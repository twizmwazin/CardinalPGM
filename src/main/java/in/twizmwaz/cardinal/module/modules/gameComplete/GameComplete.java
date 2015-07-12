package in.twizmwaz.cardinal.module.modules.gameComplete;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class GameComplete implements TaskedModule {

    protected GameComplete() {
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onObjectiveComplete(ObjectiveCompleteEvent event) {
        for (TeamModule team : Teams.getTeams()) {
            boolean skipTeam = false;
            for (GameObjective condition : Teams.getShownObjectives(team)) {
                if (!condition.isComplete() && !condition.equals(event.getObjective())) skipTeam = true;
            }
            if (Teams.getShownObjectives(team).size() == 0 || skipTeam) continue;
            GameHandler.getGameHandler().getMatch().end(team);
        }
    }

    @EventHandler
    public void onScoreUpdate(ScoreUpdateEvent event) {
        if (ScoreModule.matchHasMax()) {
            if (event.getScoreModule().getScore() >= ScoreModule.max()) {
                GameHandler.getGameHandler().getMatch().end(event.getScoreModule().getTeam());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChangeTeam(PlayerChangeTeamEvent event) {
        if (Blitz.matchIsBlitz()) {
            boolean win = true;
            TeamModule winner = null;
            for (TeamModule team : Teams.getTeams()) {
                if (!team.isObserver()) {
                    if (winner == null && team.size() > 0) {
                        winner = team;
                    } else if (winner != null && team.size() > 0) {
                        win = false;
                    }
                }
            }
            if (win) {
                GameHandler.getGameHandler().getMatch().end(winner);
            }
        }
    }

    @Override
    public void run() {
        if (TimeLimit.getMatchTimeLimit() != 0 && MatchTimer.getTimeInSeconds() >= TimeLimit.getMatchTimeLimit()) {
            GameHandler.getGameHandler().getMatch().end(TimeLimit.getMatchWinner());
        }
    }
}
