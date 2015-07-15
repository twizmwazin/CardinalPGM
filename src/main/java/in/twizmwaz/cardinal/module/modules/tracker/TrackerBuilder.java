package in.twizmwaz.cardinal.module.modules.tracker;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TrackerBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        return new ModuleCollection(new DamageTracker(), new SpleefTracker());
    }

}
