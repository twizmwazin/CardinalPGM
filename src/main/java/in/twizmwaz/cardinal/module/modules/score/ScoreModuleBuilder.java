package in.twizmwaz.cardinal.module.modules.score;

import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.jdom2.Element;

@BuilderData(load = ModuleLoadTime.LATE)
public class ScoreModuleBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        int pointsPerKill = 0;
        int pointsPerDeath = 0;
        int max = 0;
        int time = 0;
        for (Element score : match.getDocument().getRootElement().getChildren("score")) {
            if (score.getChild("limit") != null) {
                max = NumUtils.parseInt(score.getChild("limit").getText());
                if (max < 0) max = 0;
                if (max != 0) {
                    pointsPerKill = 1;
                    pointsPerDeath = 1;
                }
            }
            if (score.getChild("time") != null) {
                time = StringUtils.timeStringToSeconds(score.getChild("time").getText());
                if (time > 0) {
                    pointsPerKill = 1;
                    pointsPerDeath = 1;
                }
            }
            if (score.getChild("king") != null) {
                pointsPerKill = 0;
                pointsPerDeath = 0;
            }
            if (score.getChild("kills") != null) {
                pointsPerKill = NumUtils.parseInt(score.getChild("kills").getText());
            }
            if (score.getChild("deaths") != null) {
                pointsPerDeath = NumUtils.parseInt(score.getChild("deaths").getText());
            }
        }
        for (TeamModule team : TeamUtils.getTeams()) {
            if (!team.isObserver()) {
                results.add(new ScoreModule(team, pointsPerKill, pointsPerDeath, max));
            }
        }
        return results;
    }
}
