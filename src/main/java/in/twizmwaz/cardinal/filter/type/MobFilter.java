package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.entity.LivingEntity;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class MobFilter extends Filter {

    private final LivingEntity mob;

    public MobFilter(final LivingEntity mob) {
        this.mob = mob;
    }

    @Override
    public FilterState getState(final Object o) {
        if (o instanceof LivingEntity) {
            if (o.equals(mob)) return ALLOW;
            else return DENY;
        }
        else return ABSTAIN;
    }

}
