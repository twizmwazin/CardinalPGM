package in.twizmwaz.cardinal.module.modules.enderpearlReset;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class EnderpearlResetBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<? extends Module> load(Match match) {
        return new ModuleCollection<>(new EnderpearlReset());
    }
}
