package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AllWorldFilter extends AllEventFilter {

    public AllWorldFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        boolean abstain = true;
        for (Object object : objects) {
            if (object instanceof Event) {
                if (!(object instanceof BlockPlaceEvent) && !(object instanceof BlockBreakEvent)) {
                    return allow ? FilterState.ALLOW : FilterState.DENY;
                }
                abstain = false;
            }
        }
        if (abstain) {
            return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
        }
        return allow ? FilterState.DENY : FilterState.ALLOW;
    }
}
