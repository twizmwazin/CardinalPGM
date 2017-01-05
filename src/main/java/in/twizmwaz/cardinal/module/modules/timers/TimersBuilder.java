package in.twizmwaz.cardinal.module.modules.timers;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@LoadTime(ModuleLoadTime.EARLIEST)
public class TimersBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Countdown> load(Match match) {
        return new ModuleCollection<>(new StartTimer(match), new CycleTimer(match));
    }

}
