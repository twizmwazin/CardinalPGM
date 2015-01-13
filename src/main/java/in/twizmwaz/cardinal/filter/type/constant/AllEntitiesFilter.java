package in.twizmwaz.cardinal.filter.type.constant;

import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;

public class AllEntitiesFilter extends AllEventFilter {

    public AllEntitiesFilter(boolean allow) {
        super(allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof EntityEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
