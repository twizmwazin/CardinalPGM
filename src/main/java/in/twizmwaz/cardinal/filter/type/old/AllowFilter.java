package in.twizmwaz.cardinal.filter.type.old;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;

import java.util.Set;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.filter.FilterState.DENY;

public class AllowFilter extends Filter {

    private final Set<Filter> children;

    public AllowFilter(final Set<Filter> children) {
        this.children = children;
    }

    @Override
    public FilterState evaluate(final Event event) {
        for (Filter child : children) {
            if (child.evaluate(event).equals(ALLOW)) return ALLOW;
        }
        return DENY;
    }

}
