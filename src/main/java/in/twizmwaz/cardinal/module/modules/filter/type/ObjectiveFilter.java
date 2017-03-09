package in.twizmwaz.cardinal.module.modules.filter.type;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ObjectiveFilterParser;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class ObjectiveFilter extends FilterModule {

    private final ObjectiveFilterParser parser;
    private GameObjective objective = null;
    private TeamModule team;
    private Boolean any;

    public ObjectiveFilter(final ObjectiveFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.parser = parser;
    }

    public void load() {
        parser.load();
        this.objective = parser.getObjective();
        this.team = parser.getTeam();
        this.any = parser.isAny();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        if (objective == null) load();
        if (objective instanceof HillObjective) {
            if (any) return objective.getTeam() != null ? ALLOW : DENY;
            if (team != null) return objective.getTeam() != null && objective.getTeam().equals(team) ? ALLOW : DENY;
            for (Object object : objects) {
                if (object instanceof TeamModule) {
                    return objective.getTeam() != null && objective.getTeam().equals(object) ? ALLOW : DENY;
                } else if (object instanceof Player) {
                    Optional<TeamModule> team = Teams.getTeamOrPlayerByPlayer((Player)object);
                    return objective.getTeam() != null && team.isPresent() && objective.getTeam().equals(team.get()) ? ALLOW : DENY;
                }
            }
            return objective.getTeam() != null ? ALLOW : DENY;
        } else {
            return objective.isComplete() ? ALLOW : DENY;
        }
    }

}
