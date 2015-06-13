package in.twizmwaz.cardinal.module.modules.killStreakCount;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class KillStreakBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<KillStreakCounter> results = new ModuleCollection<KillStreakCounter>();
        results.add(new KillStreakCounter());
        return results;
    }

}
