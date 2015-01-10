package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class EntityFilter extends Filter {

    private final EntityType entity;

    public EntityFilter(final EntityType mob) {
        this.entity = mob;
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof EntityEvent) {
            if (((EntityEvent) event).getEntity().equals(entity)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
