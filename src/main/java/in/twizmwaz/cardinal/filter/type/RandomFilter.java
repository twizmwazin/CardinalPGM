package in.twizmwaz.cardinal.filter.type;

import in.twizmwaz.cardinal.filter.Filter;
import in.twizmwaz.cardinal.filter.FilterState;

import java.util.Random;

import static in.twizmwaz.cardinal.filter.FilterState.*;

public class RandomFilter extends Filter {

    private final double chance;

    public RandomFilter(final double chance) {
        this.chance = chance;
    }

    @Override
    public FilterState getState(final Object o) {
        Random random = new Random();
        double working = random.nextGaussian();
        if (working <= chance) return ALLOW;
        else return DENY;
    }

}
