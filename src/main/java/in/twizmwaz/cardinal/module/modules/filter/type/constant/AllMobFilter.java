package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import net.minecraft.server.EntityInsentient;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class AllMobFilter extends AllEventFilter {

    public AllMobFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        boolean abstain = true;
        for (Object object : objects) {
            if (object instanceof Entity) {
                if (((CraftEntity)object).getHandle() instanceof EntityInsentient) {
                    return allow ? FilterState.ALLOW : FilterState.DENY;
                }
                abstain = false;
            }
        }
        if (abstain) {
            return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
        }
        return allow ? FilterState.DENY : FilterState.ALLOW;
    }
}
