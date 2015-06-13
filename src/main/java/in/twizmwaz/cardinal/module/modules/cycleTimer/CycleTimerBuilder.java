package in.twizmwaz.cardinal.module.modules.cycleTimer;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class CycleTimerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<CycleTimerModule> results = new ModuleCollection<CycleTimerModule>();
        results.add(new CycleTimerModule(match));
        return results;
    }

}
