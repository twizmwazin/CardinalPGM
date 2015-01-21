package in.twizmwaz.cardinal.module.modules.filter.type.constant;

import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

public class AllPlayerFilter extends AllEventFilter {

    public AllPlayerFilter(final String name, final boolean allow) {
        super(name, allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof PlayerEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
