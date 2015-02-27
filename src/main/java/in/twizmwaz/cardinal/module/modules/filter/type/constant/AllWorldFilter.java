package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AllWorldFilter extends AllEventFilter {

    public AllWorldFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof BlockEvent) {
                if (object instanceof BlockPlaceEvent || object instanceof BlockBreakEvent) return FilterState.ABSTAIN;
                else return allow ? FilterState.ALLOW : FilterState.DENY;
            }
        }
        return FilterState.ABSTAIN;
    }
}
