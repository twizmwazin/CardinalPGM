package in.twizmwaz.cardinal.module.modules.deathTracker;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class DeathTrackerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DeathTracker> load(Match match) {
        return new ModuleCollection<>(new DeathTracker());
    }

}
