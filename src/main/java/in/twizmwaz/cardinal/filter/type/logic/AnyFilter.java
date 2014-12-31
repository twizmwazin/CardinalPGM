package in.twizmwaz.cardinal.filter.type.logic;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;

import java.util.Set;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class AnyFilter extends Filter {

    private final Set<Filter> children;

    public AnyFilter(final Set<Filter> children) {
        this.children = children;
    }

    @Override
    public FilterState getState(final Object o) {
        for (Filter child : children) {
            if (child.getState(o).equals(ALLOW)) return ALLOW;
        }
        return ALLOW;
    }

}
