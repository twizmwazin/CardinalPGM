package in.twizmwaz.cardinal.module.modules.teamManager;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TeamManagerModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<TeamManagerModule> results = new ModuleCollection<>();
        results.add(new TeamManagerModule(match));
        return results;
    }

}
