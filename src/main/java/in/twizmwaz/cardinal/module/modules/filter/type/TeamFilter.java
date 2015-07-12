package in.twizmwaz.cardinal.module.modules.filter.type;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.TeamFilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class TeamFilter extends FilterModule {

    private final TeamModule team;

    public TeamFilter(final TeamFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.team = parser.getTeam();
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                Optional<TeamModule> team = Teams.getTeamByPlayer((Player) object);
                if (team.isPresent())
                    if (team.get() == this.team)
                        return ALLOW;
                    else
                        return DENY;
                else
                    return DENY;
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
