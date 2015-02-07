package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.event.PgmSpawnEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;

public class AllSpawnFilter extends AllEventFilter {

    public AllSpawnFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof PgmSpawnEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        }
        return FilterState.ABSTAIN;
    }

}
