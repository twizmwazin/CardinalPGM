package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class TeamFilter extends Filter {

    private final PgmTeam team;

    protected TeamFilter(final PgmTeam team) {
        this.team = team;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof PgmTeam) {
            if (o.equals(team)) return ALLOW;
            else return DENY;
        } else if (o instanceof Player) {
            if (GameHandler.getGameHandler().getMatch().getTeam((Player) o).equals(team)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
