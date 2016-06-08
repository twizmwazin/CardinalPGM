package in.twizmwaz.cardinal.module.modules.stats;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class StatsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Stats> load(Match match) {
        return new ModuleCollection<>(new Stats());
    }

}
