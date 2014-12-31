package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.entity.Entity;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class EntityFilter extends Filter {

    private final Entity entity;

    public EntityFilter(final Entity mob) {
        this.entity = mob;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof Entity) {
            if (o.equals(entity)) return ALLOW;
            else return DENY;
        }
        else return ABSTAIN;
    }

}
