package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class AllMobFilter extends AllEventFilter {

    public AllMobFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Entity) {
                if (object instanceof LivingEntity && !(object instanceof Player)) return allow ? FilterState.ALLOW : FilterState.DENY;
                else return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
            }
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }
}
