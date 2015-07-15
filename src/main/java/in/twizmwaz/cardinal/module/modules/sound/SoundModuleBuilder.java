package in.twizmwaz.cardinal.module.modules.sound;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class SoundModuleBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection<SoundModule> load(Match match) {
        return new ModuleCollection<>(new SoundModule());
    }
}
