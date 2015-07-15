package in.twizmwaz.cardinal.module.modules.cycleTimer;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class CycleTimerModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<CycleTimerModule> load(Match match) {
        return new ModuleCollection<>(new CycleTimerModule(match));
    }

}
