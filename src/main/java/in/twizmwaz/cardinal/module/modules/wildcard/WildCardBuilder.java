package in.twizmwaz.cardinal.module.modules.wildcard;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class WildCardBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<WildCard> load(Match match) {
        return new ModuleCollection<>(new WildCard());
    }
}
