package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.KillstreakFilterParser;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.*;

public class KillStreakFilter extends FilterModule {

    private final int min;
    private final int max;
    private final int count;
    private final boolean repeat;

    public KillStreakFilter(final KillstreakFilterParser parser) {
        super(parser.getName());
        this.min = parser.getMin();
        this.max = parser.getMax();
        this.count = parser.getCount();
        this.repeat = parser.isRepeat();
    }

    @Override
    public FilterState evaluate(final Event event) {
        if (event instanceof PlayerEvent) {
            try {
                int killStreak = ((PlayerEvent) event).getPlayer().getMetadata("killstreak").get(0).asInt();
                if (this.min > -1 && this.max > -1) {
                    if (killStreak > min && killStreak < max) return ALLOW;
                    else return DENY;
                } else if (killStreak == count) return ALLOW;
                else if (repeat && killStreak % count == 0) return ALLOW;
                else return DENY;
            } catch (IndexOutOfBoundsException e) {
                return ABSTAIN;
            }
        } else return ABSTAIN;
    }

}
