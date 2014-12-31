package in.twizmwaz.cardinal.filter.type.logic;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;

import java.util.Set;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.filter.FilterState.DENY;

public class OneFilter extends Filter {

    private final Set<Filter> children;

    public OneFilter(final Set<Filter> children) {
        this.children = children;
    }

    @Override
    public FilterState getState(final Object o) {
        boolean found = false;
        for (Filter child : children) {
            if (child.getState(o).equals(ALLOW)) {
                if (!found) found = true;
                else return DENY;
            }
        }
        if (found) return ALLOW;
        else return DENY;
    }

}
