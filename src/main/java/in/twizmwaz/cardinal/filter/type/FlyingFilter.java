package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class FlyingFilter extends Filter {

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            if (((PlayerEvent) event).getPlayer().isFlying()) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
