package in.twizmwaz.cardinal.module.modules.match;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MatchModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<MatchModule> load(Match match) {
        return new ModuleCollection<>(new MatchModule(match));
    }

}
