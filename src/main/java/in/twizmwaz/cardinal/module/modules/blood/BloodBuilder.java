package in.twizmwaz.cardinal.module.modules.blood;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class BloodBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<Blood> results = new ModuleCollection<>();
        results.add(new Blood());
        return results;
    }
}
