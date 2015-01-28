package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Player;

public class AllPlayerFilter extends AllEventFilter {

    public AllPlayerFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object object) {
        if (object instanceof Player) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
