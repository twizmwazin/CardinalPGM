package in.twizmwaz.cardinal.module.modules.score;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ScoreUpdateEvent;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ScoreModule implements Module {

    private final TeamModule team;
    private int score;
    private final int pointsPerKill;
    private final int pointsPerDeath;
    private final int max;
    private final int time;

    public ScoreModule(final TeamModule team, final int pointsPerKill, final int pointsPerDeath, final int max, final int time) {
        this.team = team;
        this.score = 0;
        this.pointsPerKill = pointsPerKill;
        this.pointsPerDeath = pointsPerDeath;
        this.max = max;
        this.time = time;
    }

    @Override
    public void unload() {
    }

    public int getScore() {
        return score;
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

    public int getTime() {
        return time;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (matchHasScoring()) {
            if (event.getEntity().getKiller() != null) {
                if (TeamUtils.getTeamByPlayer(event.getEntity().getKiller()) != null) {
                    if (TeamUtils.getTeamByPlayer(event.getEntity().getKiller()) == team) {
                        score += pointsPerKill;
                        Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(this));
                    }
                }
            } else {
                if (TeamUtils.getTeamByPlayer(event.getEntity()) != null) {
                    if (TeamUtils.getTeamByPlayer(event.getEntity()) == team) {
                        score -= pointsPerDeath;
                        Bukkit.getServer().getPluginManager().callEvent(new ScoreUpdateEvent(this));
                    }
                }
            }
        }
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

    public static int getTimeLimit() {
        int time = 0;
        for (ScoreModule score : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
            time = score.getTime();
        }
        return time;
    }
}
