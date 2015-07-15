package in.twizmwaz.cardinal.module.modules.doubleKillPatch;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@BuilderData(load = ModuleLoadTime.LATEST)
public class DoubleKillPatchBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<DoubleKillPatch> load(Match match) {
        return new ModuleCollection<>(new DoubleKillPatch());
    }

}
