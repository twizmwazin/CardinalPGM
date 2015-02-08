package in.twizmwaz.cardinal.module.modules.gameComplete;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.event.ScoreboardUpdateEvent;
import in.twizmwaz.cardinal.event.objective.ObjectiveCompleteEvent;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.TaskedModule;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
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
        if (ScoreModule.matchHasMax()) {
            if (event.getScoreModule().getScore() >= ScoreModule.max()) {
                GameHandler.getGameHandler().getMatch().end(event.getScoreModule().getTeam());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(ScoreboardUpdateEvent event) {
        if (Blitz.matchIsBlitz()) {
            boolean win = true;
            TeamModule winner = null;
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver()) {
                    if (winner == null && team.getPlayers().size() > 0) {
                        winner = team;
                    } else if (winner != null && team.getPlayers().size() > 0) {
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
        if (ScoreModule.getTimeLimit() != 0) {
            if (MatchTimer.getTimeInSeconds() >= ScoreModule.getTimeLimit()) {
                TeamModule winningTeam = null;
                int winningScore = Integer.MIN_VALUE;
                boolean tied = false;
                Match match = GameHandler.getGameHandler().getMatch();
                for (ScoreModule score : match.getModules().getModules(ScoreModule.class)) {
                    if (score.getScore() > winningScore) {
                        winningTeam = score.getTeam();
                        winningScore = score.getScore();
                        tied = false;
                    } else if (score.getScore() == winningScore) {
                        tied = true;
                    }
                }
                if (tied) {
                    match.end(null);
                } else {
                    match.end(winningTeam);
                }
            }
        }
        if (Blitz.getTimeLimit() != 0) {
            if (MatchTimer.getTimeInSeconds() >= Blitz.getTimeLimit()) {
                TeamModule winningTeam = null;
                int winningAmount = Integer.MIN_VALUE;
                boolean tied = false;
                Match match = GameHandler.getGameHandler().getMatch();
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        if (team.getPlayers().size() > winningAmount) {
                            winningTeam = team;
                            winningAmount = team.getPlayers().size();
                            tied = false;
                        } else if (team.getPlayers().size() == winningAmount) {
                            tied = true;
                        }
                    }
                }
                if (tied) {
                    match.end(null);
                } else {
                    match.end(winningTeam);
                }
            }

        }
    }
}
