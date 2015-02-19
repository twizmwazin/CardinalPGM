package in.twizmwaz.cardinal.module.modules.sound;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class SoundModuleBuilder implements ModuleBuilder {
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<SoundModule> results = new ModuleCollection<>();
        results.add(new SoundModule());
        return results;
    }
}
