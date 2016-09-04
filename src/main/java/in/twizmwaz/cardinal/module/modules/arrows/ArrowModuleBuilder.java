package in.twizmwaz.cardinal.module.modules.arrows;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class ArrowModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ArrowModule> load(Match match) {
        return new ModuleCollection<>(new ArrowModule());
    }

}
