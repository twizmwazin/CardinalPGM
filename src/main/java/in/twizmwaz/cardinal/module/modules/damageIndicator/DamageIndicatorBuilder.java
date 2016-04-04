package in.twizmwaz.cardinal.module.modules.damageIndicator;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class DamageIndicatorBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DamageIndicator> load(Match match) {
        return new ModuleCollection<>(new DamageIndicator());
    }

}
