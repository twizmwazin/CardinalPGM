package in.twizmwaz.cardinal.module.modules.filter.type.logic;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.Event;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class NotFilter extends FilterModule {

    private final FilterModule childFilter;

    public NotFilter(final String name, final FilterModule childFilter) {
        super(name);
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
