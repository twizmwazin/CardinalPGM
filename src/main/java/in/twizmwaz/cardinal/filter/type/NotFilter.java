package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class NotFilter extends Filter {

    private final Filter childFilter;

    public NotFilter(final Filter childFilter) {
        this.childFilter = childFilter;
    }

    @Override
    public FilterState getState(final Object o) {
        FilterState childState = childFilter.getState(o);
        if (childState.equals(ALLOW)) return DENY;
        else if (childState.equals(DENY)) return ALLOW;
        else return ABSTAIN;
    }
}
