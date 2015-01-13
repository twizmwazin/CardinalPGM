package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;
import in.twizmwaz.cardinal.filter.parsers.RandomFilterParser;
import org.bukkit.event.Event;

import java.util.Random;

import static in.twizmwaz.cardinal.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.filter.FilterState.DENY;

public class RandomFilter extends Filter {

    private final double chance;

    public RandomFilter(final double chance) {
        this.chance = chance;
    }

    public RandomFilter(final RandomFilterParser parser) {
        this.chance = parser.getChance();
    }

    @Override
    public FilterState evaluate(final Event event) {
        Random random = new Random();
        double working = random.nextGaussian();
        if (working <= chance) return ALLOW;
        else return DENY;
    }

}
