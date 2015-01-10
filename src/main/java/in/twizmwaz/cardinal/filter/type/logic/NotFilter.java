package in.twizmwaz.cardinal.filter.type.logic;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class NotFilter extends Filter {

    private final Filter childFilter;

    public NotFilter(final Filter childFilter) {
        this.childFilter = childFilter;
    }

    @Override
    public FilterState evaluate(final Event event) {
        FilterState childState = childFilter.evaluate(event);
        if (childState.equals(ALLOW)) return DENY;
        else if (childState.equals(DENY)) return ALLOW;
        else return ABSTAIN;
    }
}
