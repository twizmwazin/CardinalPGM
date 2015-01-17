package in.twizmwaz.cardinal.module.modules.worldFreeze;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

import java.util.ArrayList;
import java.util.List;

public class WorldFreezeBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        results.add(new WorldFreeze(match));
        return results;
    }

}
