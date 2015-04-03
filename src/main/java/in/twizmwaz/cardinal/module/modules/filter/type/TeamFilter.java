package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.TeamFilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

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
                if (TeamUtils.getTeamByPlayer((Player) object) != null)
                    if (TeamUtils.getTeamByPlayer((Player) object) == team)
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
