package in.twizmwaz.cardinal.module.modules.rage;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;

@LoadTime(ModuleLoadTime.LATER)
public class RageBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<Rage> load(Match match) {
        ModuleCollection<Rage> results = new ModuleCollection<>();
        if (match.getDocument().getRootElement().getChild("rage") != null) {
            results.add(new Rage());
        }
        return results;
    }
}


