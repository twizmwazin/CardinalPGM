package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AllMobFilter extends AllEventFilter {

    public AllMobFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (object instanceof Entity) {
            if (object instanceof LivingEntity && !(object instanceof Player))
                return allow ? FilterState.ALLOW : FilterState.DENY;
            else return FilterState.ABSTAIN;
        } else return FilterState.ABSTAIN;
    }
}
