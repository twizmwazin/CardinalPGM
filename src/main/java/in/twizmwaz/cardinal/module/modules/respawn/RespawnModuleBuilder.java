package in.twizmwaz.cardinal.module.modules.respawn;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class RespawnModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<RespawnModule> load(Match match) {
        return new ModuleCollection<>(new RespawnModule(match));
    }

}
