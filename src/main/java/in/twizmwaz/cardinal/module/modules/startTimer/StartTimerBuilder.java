package in.twizmwaz.cardinal.module.modules.startTimer;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class StartTimerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<StartTimer> load(Match match) {
        return new ModuleCollection<>(new StartTimer(match, 30));
    }

}
