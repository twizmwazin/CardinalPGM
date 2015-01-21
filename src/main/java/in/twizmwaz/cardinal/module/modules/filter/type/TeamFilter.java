package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.TeamFilterParser;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class TeamFilter extends FilterModule {

    private final TeamModule team;

    public TeamFilter(final TeamFilterParser parser) {
        super(parser.getName());
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
