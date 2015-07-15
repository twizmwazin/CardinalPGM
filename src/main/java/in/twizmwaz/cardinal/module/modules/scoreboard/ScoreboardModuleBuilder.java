package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Teams;

@BuilderData(load = ModuleLoadTime.LATER)
public class ScoreboardModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ScoreboardModule> load(Match match) {
        ModuleCollection<ScoreboardModule> results = new ModuleCollection<>();
        for (TeamModule team : Teams.getTeams()) {
            results.add(new ScoreboardModule(team));
        }
        return results;
    }
}
