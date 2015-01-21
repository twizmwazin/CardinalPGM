package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;

public class AllMobFilter extends AllEventFilter {

    public AllMobFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof EntityEvent) {
            if (((EntityEvent) event).getEntity() instanceof LivingEntity && !(((EntityEvent) event).getEntity() instanceof Player))
                return allow ? FilterState.ALLOW : FilterState.DENY;
            else return FilterState.ABSTAIN;
        } else return FilterState.ABSTAIN;
    }
}
