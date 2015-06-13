package in.twizmwaz.cardinal.module.modules.wildcard;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class WildCardBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<WildCard> results = new ModuleCollection<>();
        results.add(new WildCard());
        return results;
    }
}
