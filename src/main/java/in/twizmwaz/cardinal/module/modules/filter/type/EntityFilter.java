package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.EntityFilterParser;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class EntityFilter extends FilterModule {

    private final EntityType entity;

    public EntityFilter(final EntityFilterParser parser) {
        super(parser.getName());
        this.entity = parser.getMobType();
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof EntityEvent) {
            if (((EntityEvent) event).getEntity().equals(entity)) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
