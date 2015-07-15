package in.twizmwaz.cardinal.module.modules.tntTracker;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TntTrackerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TntTracker> load(Match match) {
        return new ModuleCollection<>(new TntTracker());
    }

}
