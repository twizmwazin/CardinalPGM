package in.twizmwaz.cardinal.module.modules.visibility;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class VisibilityBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Visibility> load(Match match) {
        return new ModuleCollection<>(new Visibility(match));
    }

}
