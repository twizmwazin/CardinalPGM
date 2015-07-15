package in.twizmwaz.cardinal.module.modules.teamManager;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;

public class TeamManagerModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TeamManagerModule> load(Match match) {
        return new ModuleCollection<>(new TeamManagerModule(match));
    }

}
