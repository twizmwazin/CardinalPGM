package in.twizmwaz.cardinal.module.modules.rage;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class RageBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        if (match.getDocument().getRootElement().getChild("rage").getName().equals("rage")) {
            results.add(new Rage());
        }
        return results;
    }


}


