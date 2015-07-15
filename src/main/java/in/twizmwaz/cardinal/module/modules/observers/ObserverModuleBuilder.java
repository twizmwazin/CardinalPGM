package in.twizmwaz.cardinal.module.modules.observers;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class ObserverModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ObserverModule> load(Match match) {
        return new ModuleCollection<>(new ObserverModule(match));
    }

}
