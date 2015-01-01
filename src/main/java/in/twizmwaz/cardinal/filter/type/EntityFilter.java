package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.entity.EntityType;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class EntityFilter extends Filter {

    private final EntityType entity;

    public EntityFilter(final EntityType mob) {
        this.entity = mob;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof EntityType) {
            if (o.equals(entity)) return ALLOW;
            else return DENY;
        }
        else return ABSTAIN;
    }

}
