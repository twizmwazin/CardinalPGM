package in.twizmwaz.cardinal.module.modules.spectatorMode;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class SpectatorModeModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<SpectatorModeModule> load(Match match) {
        return new ModuleCollection<>(new SpectatorModeModule());
    }

}
