package in.twizmwaz.cardinal.module.modules.filter.type;

import in.twizmwaz.cardinal.module.modules.filter.FilterModule;
import in.twizmwaz.cardinal.module.modules.filter.FilterState;
import in.twizmwaz.cardinal.module.modules.filter.parsers.RandomFilterParser;

import java.util.Random;

import static in.twizmwaz.cardinal.module.modules.filter.FilterState.ALLOW;
import static in.twizmwaz.cardinal.module.modules.filter.FilterState.DENY;

public class RandomFilter extends FilterModule {

    private final double chance;

    public RandomFilter(final RandomFilterParser parser) {
        super(parser.getName());
        this.chance = parser.getChance();
    }

    @Override
    public FilterState evaluate(final Object object) {
        Random random = new Random();
        double working = random.nextGaussian();
        if (working <= chance) return ALLOW;
        else return DENY;
    }

}
