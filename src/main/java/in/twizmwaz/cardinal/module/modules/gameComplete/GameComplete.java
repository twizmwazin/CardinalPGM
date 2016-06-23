package in.twizmwaz.cardinal.module.modules.gameComplete;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.PlayerChangeTeamEvent;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
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
        TeamModule winner = null;
        for (TeamModule team : Teams.getTeams()) {
            if (testWin(team)) {
                if (winner == null) winner = team;
                else {
                    GameHandler.getGameHandler().getMatch().end((TeamModule) null);
                    return;
                }
            }
        }
        if (winner != null) GameHandler.getGameHandler().getMatch().end(winner);
    }

    private boolean testWin(TeamModule team) {
        ModuleCollection<GameObjective> objectives = Teams.getRequiredObjectives(team);
        for (GameObjective objective : objectives) {
            if (objective instanceof HillObjective ? (ScoreModule.matchHasScoring() || !objective.getTeam().equals(team)) : !objective.isComplete()) return false;
        }
        return objectives.size() > 0;
    }

    @EventHandler
    public void onScoreUpdate(ScoreUpdateEvent event) {
        if (ScoreModule.matchHasMax()) {
            if (event.getScoreModule().getScore() >= ScoreModule.max()) {
                GameHandler.getGameHandler().getMatch().end(event.getScoreModule().getTeam());
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
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
        if (TimeLimit.getMatchTimeLimit() != 0 && MatchTimer.getTimeInSeconds() >= TimeLimit.getMatchTimeLimit() && GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            GameHandler.getGameHandler().getMatch().end(TimeLimit.getMatchWinner());
        }
    }
}
