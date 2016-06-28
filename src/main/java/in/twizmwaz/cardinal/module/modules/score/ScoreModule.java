package in.twizmwaz.cardinal.module.modules.score;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.CardinalDeathEvent;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;

public class ScoreModule implements Module {

    private static boolean scoring = false;
    private static int pointsPerKill;
    private static int pointsPerDeath;
    private static int max;

    private final TeamModule team;
    private double score;

    public ScoreModule(final TeamModule team) {
        this.team = team;
        this.score = 0;
    }

    public static void setup(boolean scoring, int pointsPerKill, int pointsPerDeath, int max) {
        ScoreModule.scoring = scoring;
        ScoreModule.pointsPerKill = pointsPerKill;
        ScoreModule.pointsPerDeath = pointsPerDeath;
        ScoreModule.max = max;
    }

    public static boolean matchHasScoring() {
        return scoring;
    }

    public static boolean matchHasMax() {
        return max != 0;
    }

    public static int max() {
        return max;
    }

    public static ScoreModule getScoreModule(TeamModule team) {
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
            if (score.getTeam().equals(team)) return score;
        }
        return null;
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCardinalDeath(CardinalDeathEvent event) {
        if (matchHasScoring()) {
            TeamModule killer = event.getKiller() == null ? null : Teams.getTeamOrPlayerByPlayer(event.getKiller()).orNull();
            TeamModule dead = Teams.getTeamOrPlayerByPlayer(event.getPlayer()).orNull();
            if (killer != null && killer != dead) {
                if (killer == team) addScore(pointsPerKill);
            } else if (dead == team) {
                addScore(pointsPerDeath * -1);
            }
        }
    }

}
