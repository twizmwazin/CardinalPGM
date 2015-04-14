package in.twizmwaz.cardinal.module.modules.antimob;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@BuilderData(load = ModuleLoadTime.EARLIEST)
public class AntiMobBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<AntiMobModule> results = new ModuleCollection<AntiMobModule>();
        if (Cardinal.getInstance().getConfig().getBoolean("antiMob")) {
            results.add(new AntiMobModule(match));
        }
        return results;
    }

}
