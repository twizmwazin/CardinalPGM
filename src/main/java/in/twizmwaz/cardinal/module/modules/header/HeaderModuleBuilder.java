package in.twizmwaz.cardinal.module.modules.header;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;

@BuilderData(load = ModuleLoadTime.LATEST)
public class HeaderModuleBuilder implements ModuleBuilder {
    
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<HeaderModule> results = new ModuleCollection<HeaderModule>();
        results.add(new HeaderModule(match.getLoadedMap()));
        return results;
    }
}
