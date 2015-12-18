package in.twizmwaz.cardinal.module.modules.multitrade;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class MultitradeBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Multitrade> load(Match match) {
        return new ModuleCollection<>(new Multitrade());
    }

}
