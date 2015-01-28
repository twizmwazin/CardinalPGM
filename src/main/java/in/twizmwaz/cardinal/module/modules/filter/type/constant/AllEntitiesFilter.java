package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Entity;

public class AllEntitiesFilter extends AllEventFilter {

    public AllEntitiesFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (object instanceof Entity) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
