package in.twizmwaz.cardinal.module.modules.score;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

public class ScoreModule implements Module {

    private final TeamModule team;
    private final int pointsPerKill;
    private final int pointsPerDeath;
    private final int max;
    private double score;

    public ScoreModule(final TeamModule team, final int pointsPerKill, final int pointsPerDeath, final int max) {
        this.team = team;
        this.score = 0;
        this.pointsPerKill = pointsPerKill;
        this.pointsPerDeath = pointsPerDeath;
        this.max = max;
    }

    public static boolean matchHasScoring() {
        return matchHasPointsPerKill() || matchHasPointsPerDeath() || matchHasMax();
    }

    public static boolean matchHasPointsPerKill() {
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
            if (score.getPointsPerKill() != 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean matchHasPointsPerDeath() {
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
            if (score.getPointsPerDeath() != 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean matchHasMax() {
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
            if (score.getMax() != 0) {
                return true;
            }
        }
        return false;
    }

    public static int max() {
        int max = 0;
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
            max = score.getMax();
        }
        return max;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public int getScore() {
        return (int)score;
    }

    public void setScore(int score) {
        int oldPoints = getScore();
        this.score = score;
        if (getScore() != oldPoints)
            Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(this));
    }

    public void addScore(double score) {
        int oldPoints = getScore();
        this.score += score;
        if (getScore() != oldPoints)
            Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(this));
    }

    public int getPointsPerKill() {
        return pointsPerKill;
    }

    public int getPointsPerDeath() {
        return pointsPerDeath;
    }

    public int getMax() {
        return max;
    }

    public TeamModule getTeam() {
        return team;
    }

    @EventHandler
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (matchHasScoring()) {
            if (event.getKiller() != null) {
                if (Teams.getTeamByPlayer(event.getKiller()).isPresent() && Teams.getTeamByPlayer(event.getKiller()).get().equals(team)) {
                    score += pointsPerKill;
                    Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(this));
                }
            } else {
                if (Teams.getTeamByPlayer(event.getPlayer()).isPresent() && Teams.getTeamByPlayer(event.getPlayer()).get().equals(team)) {
                    score -= pointsPerKill;
                    Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(this));
                }
            }
        }
    }
}
