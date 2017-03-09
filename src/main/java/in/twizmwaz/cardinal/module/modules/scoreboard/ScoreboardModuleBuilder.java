package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Parser;
import in.twizmwaz.cardinal.util.Teams;

@LoadTime(ModuleLoadTime.LATER)
public class ScoreboardModuleBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<ScoreboardModule> load(Match match) {
        ModuleCollection<ScoreboardModule> results = new ModuleCollection<>();
        ScoreboardModule.setTitle(Blitz.getTitle() != null ? Blitz.getTitle() : Parser.getOrderedAttribute("game", match.getDocument().getRootElement()));
        for (TeamModule team : Teams.getTeamsAndPlayerManager()) {
            results.add(new ScoreboardModule(team));
        }
        return results;
    }
}
