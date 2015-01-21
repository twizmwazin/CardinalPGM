package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockEvent;

public class AllBlockFilter extends AllEventFilter {

    public AllBlockFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof BlockEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }
}
