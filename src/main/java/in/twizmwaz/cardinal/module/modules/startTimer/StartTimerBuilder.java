package in.twizmwaz.cardinal.module.modules.startTimer;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class StartTimerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<StartTimer> results = new ModuleCollection<StartTimer>();
        results.add(new StartTimer(match, 30));
        return results;
    }

}
