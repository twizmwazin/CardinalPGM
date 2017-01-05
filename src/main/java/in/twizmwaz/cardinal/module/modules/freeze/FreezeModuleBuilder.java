package in.twizmwaz.cardinal.module.modules.freeze;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@LoadTime(ModuleLoadTime.LATE)
public class FreezeModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<FreezeModule> load(Match match) {
        return new ModuleCollection<>(new FreezeModule(match));
    }

}
