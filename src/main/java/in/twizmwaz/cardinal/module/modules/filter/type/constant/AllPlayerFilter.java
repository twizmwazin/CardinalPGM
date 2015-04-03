package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ABSTAIN;

public class AllPlayerFilter extends AllEventFilter {

    public AllPlayerFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(final Object... objects) {
        for (Object object : objects) {
            if (object instanceof Player) return allow ? FilterState.ALLOW : FilterState.DENY;
        }
        return (getParent() == null ? ABSTAIN : getParent().evaluate(objects));
    }

}
