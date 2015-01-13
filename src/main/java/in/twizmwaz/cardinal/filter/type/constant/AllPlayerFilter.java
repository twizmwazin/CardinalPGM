package in.twizmwaz.cardinal.filter.type.constant;

import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

public class AllPlayerFilter extends AllEventFilter {

    public AllPlayerFilter(boolean allow) {
        super(allow);
    }

    @Override
    public FilterState evaluate(Event event) {
        if (event instanceof PlayerEvent) return allow ? FilterState.ALLOW : FilterState.DENY;
        else return FilterState.ABSTAIN;
    }

}
