package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.filter.parsers.TeamFilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class TeamFilter extends Filter {

    private final TeamModule team;

    public TeamFilter(final TeamModule team) {
        this.team = team;
    }

    public TeamFilter(final TeamFilterParser parser) {
        this.team = parser.getTeam();

    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            if (TeamUtils.getTeamByPlayer(((PlayerEvent) event).getPlayer()).equals(team))
                return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
