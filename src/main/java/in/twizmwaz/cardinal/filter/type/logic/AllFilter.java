package in.twizmwaz.cardinal.filter.type.logic;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;

import java.util.Set;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.filter.FilterState.DENY;

public class AllFilter extends Filter {

    private final Set<Filter> children;

    public AllFilter(final Set<Filter> children) {
        this.children = children;
    }

    @Override
    public FilterState evaluate(final Event event) {
        for (Filter child : children) {
            if (child.evaluate(event).equals(DENY)) return DENY;
        }
        return ALLOW;
    }

}
