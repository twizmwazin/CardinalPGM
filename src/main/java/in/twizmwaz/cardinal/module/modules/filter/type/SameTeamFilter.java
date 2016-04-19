package in.twizmwaz.cardinal.module.modules.filter.type;

import com.google.common.base.Optional;
import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.ChildFilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.entity.Player;

import java.util.List;

public class SameTeamFilter extends FilterModule {

    private final FilterModule child;

    public SameTeamFilter(ChildFilterParser parser) {
        super(parser.getName(), parser.getParent());
        this.child = parser.getChild();
    }

    @Override
    public FilterState evaluate(Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) {
                Optional<TeamModule> team = Teams.getTeamByPlayer((Player) object);
                if (!team.isPresent()) continue;
                for (Player player : (List<Player>) team.get()) {
                    if (child.evaluate(player).equals(FilterState.ALLOW)) return FilterState.ALLOW;
                }
                return FilterState.DENY;
            }
        }
        return (getParent() == null ? FilterState.ABSTAIN : getParent().evaluate(objects));
    }

}
