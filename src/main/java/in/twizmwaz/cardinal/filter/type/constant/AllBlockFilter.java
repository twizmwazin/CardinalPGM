package in.twizmwaz.cardinal.filter.type.constant;

import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;

public class AllBlockFilter extends AllEventFilter {

    public AllBlockFilter(boolean allow) {
        super(allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof BlockEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }
}
