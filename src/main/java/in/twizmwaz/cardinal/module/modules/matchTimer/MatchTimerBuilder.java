package in.twizmwaz.cardinal.module.modules.matchTimer;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MatchTimerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MatchTimer> load(Match match) {
        return new ModuleCollection<>(new MatchTimer());
    }

}
