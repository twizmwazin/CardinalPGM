package in.twizmwaz.cardinal.module.modules.rank;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class RankModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<RankModule> load(Match match) {
        return new ModuleCollection<>(new RankModule());
    }

}
