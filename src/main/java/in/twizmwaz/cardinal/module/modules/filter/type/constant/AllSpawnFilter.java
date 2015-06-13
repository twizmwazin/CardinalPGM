package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.event.CardinalSpawnEvent;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class AllSpawnFilter extends AllEventFilter {

    public AllSpawnFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof CardinalSpawnEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
