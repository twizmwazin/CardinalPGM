package in.twizmwaz.cardinal.module.modules.spectatorTools;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class SpectatorToolsBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<SpectatorTools> load(Match match) {
        return new ModuleCollection<>(new SpectatorTools());
    }

}
