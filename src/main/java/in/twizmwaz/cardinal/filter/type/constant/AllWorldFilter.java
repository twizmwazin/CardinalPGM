package in.twizmwaz.cardinal.filter.type.constant;

import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AllWorldFilter extends AllEventFilter {

    public AllWorldFilter(boolean allow) {
        super(allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof BlockEvent) {
            if (event instanceof BlockPlaceEvent || event instanceof BlockBreakEvent) return FilterState.ABSTAIN;
            else return allow ? FilterState.ALLOW : FilterState.DENY;
        } else return FilterState.ABSTAIN;
    }
}
