package in.twizmwaz.cardinal.filter.type.constant;

import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;

public class AllSpawnFilter extends AllEventFilter {

    public AllSpawnFilter(boolean allow) {
        super(allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof PgmSpawnEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
