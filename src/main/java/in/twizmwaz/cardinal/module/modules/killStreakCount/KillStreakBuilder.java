package in.twizmwaz.cardinal.module.modules.killStreakCount;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class KillStreakBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<KillStreakCounter> load(Match match) {
        return new ModuleCollection<>(new KillStreakCounter());
    }

}
