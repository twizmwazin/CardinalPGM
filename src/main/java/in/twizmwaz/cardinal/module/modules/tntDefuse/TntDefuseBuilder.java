package in.twizmwaz.cardinal.module.modules.tntDefuse;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TntDefuseBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TntDefuse> load(Match match) {
        return new ModuleCollection<>(new TntDefuse());
    }

}
