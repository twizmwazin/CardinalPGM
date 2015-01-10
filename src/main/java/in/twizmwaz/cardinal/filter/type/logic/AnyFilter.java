package in.twizmwaz.cardinal.filter.type.logic;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;

import java.util.Set;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;

public class AnyFilter extends Filter {

    private final Set<Filter> children;

    public AnyFilter(final Set<Filter> children) {
        this.children = children;
    }

    @Override
    public FilterState evaluate(final Event event) {
        for (Filter child : children) {
            if (child.evaluate(event).equals(ALLOW)) return ALLOW;
        }
        return ALLOW;
    }

}
