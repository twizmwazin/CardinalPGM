package in.twizmwaz.cardinal.module.modules.blood;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class BloodBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<Blood> load(Match match) {
        return new ModuleCollection<>(new Blood());
    }
}
