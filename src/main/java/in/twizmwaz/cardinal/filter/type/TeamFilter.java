package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class TeamFilter extends Filter {

    private final PgmTeam team;

    protected TeamFilter(final PgmTeam team) {
        this.team = team;
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            if (GameHandler.getGameHandler().getMatch().getTeam(((PlayerEvent) event).getPlayer()).equals(team)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
