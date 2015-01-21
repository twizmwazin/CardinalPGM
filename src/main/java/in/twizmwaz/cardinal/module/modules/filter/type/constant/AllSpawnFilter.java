package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.Event;

public class AllSpawnFilter extends AllEventFilter {

    public AllSpawnFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof PgmSpawnEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
