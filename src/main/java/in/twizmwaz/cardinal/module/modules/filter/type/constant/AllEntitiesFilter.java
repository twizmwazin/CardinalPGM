package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Entity;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class AllEntitiesFilter extends AllEventFilter {

    public AllEntitiesFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Entity) return allow ? FilterState.ALLOW : FilterState.DENY;
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
