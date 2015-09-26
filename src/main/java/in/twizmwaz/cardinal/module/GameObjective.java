package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;

public interface GameObjective extends Module {

    String getName();

    String getId();

    boolean isRequired();

    TeamModule getTeam();

    boolean isTouched();

    boolean isComplete();

    boolean show();

    GameObjectiveScoreboardHandler getScoreboardHandler();

    enum ProximityMetric {
        CLOSEST_PLAYER, CLOSEST_BLOCK, CLOSEST_KILL;

        public static ProximityMetric matchProximityMetric(String metric) {
            if (metric == null) return null;
            return ProximityMetric.valueOf(metric.toUpperCase().replaceAll(" ", "_"));
        }
    }

}
