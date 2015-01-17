package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@BuilderData(load = ModuleLoadTime.LATEST)
public class GameScoreboardBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        return results;
    }
}
