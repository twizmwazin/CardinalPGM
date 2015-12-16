package in.twizmwaz.cardinal.module;

import in.twizmwaz.cardinal.module.modules.scoreboard.GameObjectiveScoreboardHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;

public interface GameObjective extends Module {

    public TeamModule getTeam();

    public String getName();

    public String getId();

    public boolean isTouched();

    public boolean isComplete();

    public boolean showOnScoreboard();

    public boolean isRequired();

    public GameObjectiveScoreboardHandler getScoreboardHandler();

}
