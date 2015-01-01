package in.twizmwaz.cardinal.filter.type.old;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;

import java.util.Set;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.filter.FilterState.DENY;

public class DenyFilter extends Filter {

    private final Set<Filter> children;

    public DenyFilter(final Set<Filter> children) {
        this.children = children;
    }

    @Override
    public FilterState getState(final Object o) {
        for (Filter child : children) {
            if (child.getState(o).equals(ALLOW)) return DENY;
        }
        return ALLOW;
    }
}
