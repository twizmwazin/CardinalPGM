package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.filter.parsers.KillstreakFilterParser;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class KillStreakFilter extends Filter {

    private final int min;
    private final int max;
    private final int count;
    private final boolean repeat;

    public KillStreakFilter(final int min, final int max, final int count, final boolean repeat) {
        this.min = min;
        this.max = max;
        this.count = count;
        this.repeat = repeat;
    }
    
    public KillStreakFilter(final KillstreakFilterParser parser) {
        this.min = parser.getMin();
        this.max = parser.getMax();
        this.count = parser.getCount();
        this.repeat = parser.isRepeat();
    }

    public KillStreakFilter(final int min, final int max) {
        this(min, max, -1, false);
    }

    public KillStreakFilter(final int count, final boolean repeat) {
        this(-1, -1, count, repeat);
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
