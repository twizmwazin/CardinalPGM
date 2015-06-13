package in.twizmwaz.cardinal.module.modules.stats;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class StatsBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        results.add(new Stats());
        return results;
    }
}
