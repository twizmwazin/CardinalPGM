package in.twizmwaz.cardinal.module.modules.timeLimit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.HandlerList;

public class TimeLimit implements Module {

    private int time;
    private final Result result;
    private final TeamModule winner;

    public enum Result {
        TEAM(), OBJECTIVES(), TIE()
    }

    protected TimeLimit(int time, final Result result, final TeamModule winner) {
        this.time = time;
        this.result = result;
        this.winner = winner;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public int getTimeLimit() {
        return time;
    }

    public Result getResult() {
        return result;
    }

    public TeamModule getWinner() {
        return winner;
    }

    public static TeamModule getMatchWinner() {
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class) == null) return null;
        TimeLimit limit = GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class);
        if (limit.getResult().equals(Result.TEAM)) {
            return limit.getWinner();
        } else if (limit.getResult().equals(Result.OBJECTIVES)) {
            int points = 0;
            TeamModule winning = null;
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver()) {
                    int teamPoints = 0;
                    for (GameObjective objective : TeamUtils.getShownObjectives(team)) {
                        if (objective.isComplete()) teamPoints += 100;
                        else if (objective.isTouched() && objective instanceof DestroyableObjective) teamPoints += ((DestroyableObjective) objective).getPercent();
                        else if (objective.isTouched()) teamPoints += 50;
                    }
                    if (teamPoints > points) {
                        points = teamPoints;
                        winning = team;
                    } else if (teamPoints == points) {
                        winning = null;
                    }
                }
            }
            return winning;
        }
        return null;
    }

    public static int getMatchTimeLimit() {
        for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
            return module.getTimeLimit();
        }
        return 0;
    }

    public void setTimeLimit(int time) {
        this.time = time;
    }

}
