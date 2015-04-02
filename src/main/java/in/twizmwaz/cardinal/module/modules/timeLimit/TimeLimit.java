package in.twizmwaz.cardinal.module.modules.timeLimit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.HandlerList;

public class TimeLimit implements Module {

    private int time;
    private Result result;
    private TeamModule team;

    public enum Result {
        TEAM(), MOST_OBJECTIVES(), TIE(), MOST_PLAYERS(), HIGHEST_SCORE()
    }

    protected TimeLimit(int time, Result result, TeamModule team) {
        this.time = time;
        this.result = result;
        this.team = team;
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

    public void setResult(Result result) {
        this.result = result;
    }

    public TeamModule getTeam() {
        return team;
    }

    public void setTeam(TeamModule team) {
        this.team = team;
    }

    public static TeamModule getMatchWinner() {
        TeamModule winner = null;
        TimeLimit timeLimit = GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class);
        if (timeLimit != null) {
            if (timeLimit.getResult().equals(Result.TEAM)) {
                winner = timeLimit.getTeam();
            } else if (timeLimit.getResult().equals(Result.MOST_OBJECTIVES)) {
                int completed = Integer.MIN_VALUE;
                int touched = Integer.MIN_VALUE;
                int touchedPoints = Integer.MIN_VALUE;
                double proximity = Double.POSITIVE_INFINITY;
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        int teamCompleted = 0;
                        int teamTouched = 0;
                        int teamTouchedPoints = 0;
                        double teamProximity = Double.POSITIVE_INFINITY;
                        boolean safetyProximity = false;
                        for (GameObjective obj : TeamUtils.getShownObjectives(team)) {
                            if (obj.isComplete()) {
                                teamCompleted ++;
                            } else if (obj.isTouched()) {
                                teamTouched ++;
                                if (obj instanceof WoolObjective) {
                                    teamTouchedPoints += 50;
                                    if (((WoolObjective) obj).getProximity() < teamProximity || !safetyProximity) {
                                        teamProximity = ((WoolObjective) obj).getProximity();
                                        safetyProximity = true;
                                    }
                                } else if (obj instanceof CoreObjective) {
                                    teamTouchedPoints += 50;
                                } else if (obj instanceof DestroyableObjective) {
                                    teamTouchedPoints += ((DestroyableObjective) obj).getPercent();
                                }
                            } else {
                                if (obj instanceof WoolObjective && !safetyProximity) {
                                    if (((WoolObjective) obj).getProximity() < teamProximity) {
                                        teamProximity = ((WoolObjective) obj).getProximity();
                                    }
                                } else if (obj instanceof CoreObjective) {
                                    if (((CoreObjective) obj).getProximity() < teamProximity) {
                                        teamProximity = ((CoreObjective) obj).getProximity();
                                    }
                                } else if (obj instanceof DestroyableObjective) {
                                    if (((DestroyableObjective) obj).getProximity() < teamProximity) {
                                        teamProximity = ((DestroyableObjective) obj).getProximity();
                                    }
                                }
                            }
                        }
                        if (teamCompleted > completed) {
                            winner = team;
                            completed = teamCompleted;
                            touched = teamTouched;
                            touchedPoints = teamTouchedPoints;
                            proximity = teamProximity;
                        } else if (teamCompleted == completed) {
                            if (teamTouched > touched) {
                                winner = team;
                                touched = teamTouched;
                                touchedPoints = teamTouchedPoints;
                                proximity = teamProximity;
                            } else if (teamTouched == touched) {
                                if (teamTouchedPoints > touchedPoints) {
                                    winner = team;
                                    touchedPoints = teamTouchedPoints;
                                    proximity = teamProximity;
                                } else if (teamTouchedPoints == touchedPoints) {
                                    if (teamProximity < proximity) {
                                        winner = team;
                                        proximity = teamProximity;
                                    } else if (teamProximity == proximity) {
                                        winner = null;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (timeLimit.getResult().equals(Result.TIE)) {
                // Winner stays null
            } else if (timeLimit.getResult().equals(Result.MOST_PLAYERS)) {
                int players = Integer.MIN_VALUE;
                for (TeamModule team : TeamUtils.getTeams()) {
                    if (!team.isObserver()) {
                        if (team.size() > players) {
                            winner = team;
                            players = team.size();
                        } else if (team.size() == players) {
                            winner = null;
                        }
                    }
                }
            } else if (timeLimit.getResult().equals(Result.HIGHEST_SCORE)) {
                int score = Integer.MIN_VALUE;
                for (ScoreModule scoreModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    if (scoreModule.getScore() > score) {
                        winner = scoreModule.getTeam();
                        score = scoreModule.getScore();
                    } else if (scoreModule.getScore() == score) {
                        winner = null;
                    }
                }
            }
        }
        return winner;
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
