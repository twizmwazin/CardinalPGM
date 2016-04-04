package in.twizmwaz.cardinal.module.modules.attackSpeed;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class AttackSpeedBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<AttackSpeed> load(Match match) {
        return new ModuleCollection<>(new AttackSpeed());
    }

}
