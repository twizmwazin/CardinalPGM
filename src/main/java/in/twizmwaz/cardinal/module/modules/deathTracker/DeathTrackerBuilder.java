package in.twizmwaz.cardinal.module.modules.deathTracker;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class DeathTrackerBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<DeathTracker> results = new ModuleCollection<>();
        results.add(new DeathTracker());
        return results;
    }

}
