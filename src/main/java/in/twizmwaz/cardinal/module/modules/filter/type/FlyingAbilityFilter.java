package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterParser;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class FlyingAbilityFilter extends FilterModule {

    public FlyingAbilityFilter(FilterParser parser) {
        super(parser.getName());
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            if (((PlayerEvent) event).getPlayer().getAllowFlight()) return ALLOW;
            else return DENY;
        } else return ABSTAIN;
    }

}
