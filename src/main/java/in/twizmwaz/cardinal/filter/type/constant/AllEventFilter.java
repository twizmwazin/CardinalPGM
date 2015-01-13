package in.twizmwaz.cardinal.filter.type.constant;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;

public class AllEventFilter extends Filter {

    protected final boolean allow;

    public AllEventFilter(boolean allow) {
        this.allow = allow;
    }

    @Override
    public FilterState evaluate(Event event) {
        return allow ? FilterState.ALLOW : FilterState.DENY;
    }

}
