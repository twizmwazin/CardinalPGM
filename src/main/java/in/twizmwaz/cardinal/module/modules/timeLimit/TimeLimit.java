package in.twizmwaz.cardinal.module.modules.timeLimit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timeNotifications.TimeNotifications;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimeLimit implements Module {

    private int time;
    private Result result;
    private TeamModule team;

    protected TimeLimit(int time, Result result, TeamModule team) {
        this.time = time;
        this.result = result;
        this.team = team;
    }

    public static TeamModule getMatchWinner() {
        ModuleCollection <TeamModule> sortedTeams = TimeLimit.getSortedTeams();
        return (sortedTeams.size() < 2 || getWinningTeam(sortedTeams.get(sortedTeams.size() - 1), sortedTeams.get(sortedTeams.size() - 2)) != 0) ? sortedTeams.get(sortedTeams.size() - 1) : null;
    }

    public static ModuleCollection<TeamModule> getSortedTeams() {
        ModuleCollection <TeamModule> winnerList = Teams.getTeams();
        Collections.sort(winnerList, new Comparator<TeamModule>() {
            @Override
            public int compare(TeamModule team1, TeamModule team2) {
                return getWinningTeam(team1, team2);
            }
        });
        return winnerList;
    }

    public static int getWinningTeam(TeamModule team1, TeamModule team2){
        if (team1.isObserver()) return -1;
        if (team2.isObserver()) return 1;
        TimeLimit timeLimit = GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class);
        if (timeLimit != null) {
            if (timeLimit.getResult().equals(Result.TEAM)) {
                if (team1.equals(timeLimit.getTeam())) return 1;
                if (team2.equals(timeLimit.getTeam())) return -1;
            } else if (timeLimit.getResult().equals(Result.MOST_OBJECTIVES)) {
                int completedObjectives1 = 0, completedObjectives2 = 0;
                int touchedObjectives1 = 0, touchedObjectives2 = 0;

                for (GameObjective obj : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
                    if (obj.isRequired() && obj.isComplete()){
                        if (obj instanceof WoolObjective){
                            if (obj.getTeam().equals(team1)) completedObjectives1++;
                            if (obj.getTeam().equals(team2)) completedObjectives2++;
                        } else if (!(obj instanceof HillObjective)){
                            if (!obj.getTeam().equals(team1)) completedObjectives1++;
                            if (!obj.getTeam().equals(team2)) completedObjectives2++;
                        }
                    } else if (obj.isRequired() && obj.isTouched()){
                        if (obj instanceof WoolObjective){
                            if (obj.getTeam().equals(team1)) touchedObjectives1++;
                            if (obj.getTeam().equals(team2)) touchedObjectives2++;
                        } else if (!(obj instanceof HillObjective)){
                            if (!obj.getTeam().equals(team1)) touchedObjectives1++;
                            if (!obj.getTeam().equals(team2)) touchedObjectives2++;
                        }
                    }
                    if (obj instanceof HillObjective) {
                        if (obj.getTeam() != null) {
                            if (obj.getTeam().equals(team1)) completedObjectives1++;
                            if (obj.getTeam().equals(team2)) completedObjectives2++;
                        } else if (((HillObjective)obj).getCapturingTeam() != null) {
                            if (((HillObjective)obj).getCapturingTeam().equals(team1)) touchedObjectives1++;
                            if (((HillObjective)obj).getCapturingTeam().equals(team2)) touchedObjectives2++;
                        }
                    }
                }
                if (completedObjectives1 != completedObjectives2) return completedObjectives1 - completedObjectives2;
                if (touchedObjectives1 != touchedObjectives2) return touchedObjectives1 - touchedObjectives2;

                if (touchedObjectives1 != 0){
                    List<Integer> closestCompletion1 = new ArrayList<Integer>(){};
                    List<Integer> closestCompletion2 = new ArrayList<Integer>(){};
                    List<Double> proximity1 = new ArrayList<Double>(){};
                    List<Double> proximity2 = new ArrayList<Double>(){};
                    for (GameObjective obj : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
                        if (obj.isRequired() && obj.isTouched()){
                            if (obj instanceof WoolObjective){
                                if (obj.getTeam().equals(team1)) {
                                    closestCompletion1.add(50);
                                    proximity1.add(((WoolObjective) obj).getProximity());
                                }
                                if (obj.getTeam().equals(team2)) {
                                    closestCompletion2.add(50);
                                    proximity2.add(((WoolObjective) obj).getProximity());
                                }
                            } else if (obj instanceof CoreObjective){
                                if (!obj.getTeam().equals(team1)) closestCompletion1.add(50);
                                if (!obj.getTeam().equals(team2)) closestCompletion2.add(50);
                            } else if (obj instanceof DestroyableObjective){
                                if (!obj.getTeam().equals(team1)) closestCompletion1.add(((DestroyableObjective) obj).getPercent());
                                if (!obj.getTeam().equals(team2)) closestCompletion2.add(((DestroyableObjective) obj).getPercent());
                            }
                        }
                    }
                    Collections.sort(closestCompletion1, Collections.reverseOrder());
                    Collections.sort(closestCompletion2, Collections.reverseOrder());
                    for (int i = 0; i < closestCompletion1.size(); i++) {
                        if (!closestCompletion1.get(i).equals(closestCompletion2.get(i))) return closestCompletion1.get(i) - closestCompletion2.get(i);
                    }
                    Collections.sort(proximity1);
                    Collections.sort(proximity2);
                    int prox = proximity1.size() > proximity2.size() ? proximity1.size() : proximity2.size();
                    for (int i = 0; i < prox; i++) {
                        Double prox1 = proximity1.get(i) != null ? proximity1.get(i) : Double.POSITIVE_INFINITY;
                        Double prox2 = proximity2.get(i) != null ? proximity2.get(i) : Double.POSITIVE_INFINITY;
                        if (!prox1.equals(prox2)) return prox1 < prox2 ? 1 : -1;
                    }
                }
                List<Double> proximity1 = new ArrayList<Double>(){};
                List<Double> proximity2 = new ArrayList<Double>(){};
                for (GameObjective obj : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
                    if (obj.isRequired() && !obj.isComplete() && !obj.isTouched()){
                        if (obj instanceof WoolObjective){
                            if (obj.getTeam().equals(team1)) proximity1.add(((WoolObjective) obj).getProximity());
                            if (obj.getTeam().equals(team2)) proximity2.add(((WoolObjective) obj).getProximity());
                        } else if (obj instanceof CoreObjective){
                            if (!obj.getTeam().equals(team1)) proximity1.add(((CoreObjective) obj).getProximity());
                            if (!obj.getTeam().equals(team2)) proximity2.add(((CoreObjective) obj).getProximity());
                        } else if (obj instanceof DestroyableObjective){
                            if (!obj.getTeam().equals(team1)) proximity1.add(((DestroyableObjective) obj).getProximity());
                            if (!obj.getTeam().equals(team2)) proximity2.add(((DestroyableObjective) obj).getProximity());
                        }
                    }
                }
                Collections.sort(proximity1);
                Collections.sort(proximity2);
                int prox = proximity1.size() > proximity2.size() ? proximity1.size() : proximity2.size();
                for (int i = 0; i < prox; i++) {
                    Double prox1 = Double.POSITIVE_INFINITY;
                    Double prox2 = Double.POSITIVE_INFINITY;
                    try {
                        prox1 = proximity1.get(i);
                        prox2 = proximity2.get(i);
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                    if (!prox1.equals(prox2)) return prox1 < prox2 ? 1 : -1;
                }
            } else if (timeLimit.getResult().equals(Result.TIE)) {
                return 0;
            } else if (timeLimit.getResult().equals(Result.MOST_PLAYERS)) {
                return team1.size() - team2.size();
            } else if (timeLimit.getResult().equals(Result.HIGHEST_SCORE)) {
                int score1 = Integer.MIN_VALUE,score2 = Integer.MIN_VALUE;
                for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    if (scoreModule.getTeam().equals(team1)) score1 = scoreModule.getScore();
                    if (scoreModule.getTeam().equals(team2)) score2 = scoreModule.getScore();
                }
                return score1 - score2;
            }
        }
        return 0;
    }

    public static int getMatchTimeLimit() {
        for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
            return module.getTimeLimit();
        }
        return 0;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public int getTimeLimit() {
        return time;
    }

    public void setTimeLimit(int time) {
        this.time = time;
        GameHandler.getGameHandler().getMatch().getModules().getModule(TimeNotifications.class).changeTime(time);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public TeamModule getTeam() {
        return team;
    }

    public void setTeam(TeamModule team) {
        this.team = team;
    }

    public enum Result {
        TEAM(), MOST_OBJECTIVES(), TIE(), MOST_PLAYERS(), HIGHEST_SCORE()
    }

}
