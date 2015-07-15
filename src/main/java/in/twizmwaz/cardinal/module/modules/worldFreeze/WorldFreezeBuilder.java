package in.twizmwaz.cardinal.module.modules.worldFreeze;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class WorldFreezeBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<WorldFreeze> load(Match match) {
        return new ModuleCollection<>(new WorldFreeze(match));
    }

}
