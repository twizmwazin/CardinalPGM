package in.twizmwaz.cardinal.module.modules.timers;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TimersBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Countdown> load(Match match) {
        return new ModuleCollection<>(new StartTimer(match), new CycleTimer(match));
    }

}
