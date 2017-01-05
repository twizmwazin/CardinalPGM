package in.twizmwaz.cardinal.module.modules.timeLimit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.LoadTime;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
import org.jdom2.Element;

@LoadTime(ModuleLoadTime.LATE)
public class TimeLimitBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection<TimeLimit> load(Match match) {
        ModuleCollection<TimeLimit> results = new ModuleCollection<>();
        int timeLimit = 0;
        TimeLimit.Result result = null;
        TeamModule team = null;
        for (Element time : match.getDocument().getRootElement().getChildren("time")) {
            timeLimit = Strings.timeStringToSeconds(time.getText());
            if (time.getAttributeValue("result") != null) {
                if (time.getAttributeValue("result").equalsIgnoreCase("objectives")) {
                    result = TimeLimit.Result.MOST_OBJECTIVES;
                } else if (time.getAttributeValue("result").equalsIgnoreCase("tie")) {
                    result = TimeLimit.Result.TIE;
                } else {
                    result = TimeLimit.Result.TEAM;
                    team = Teams.getTeamById(time.getAttributeValue("result")).orNull();
                }
            }
        }
        for (Element score : match.getDocument().getRootElement().getChildren("score")) {
            if (result == null) result = TimeLimit.Result.HIGHEST_SCORE;
            if (score.getChild("time") != null) timeLimit = Strings.timeStringToSeconds(score.getChild("time").getText());
        }
        for (Element blitz : match.getDocument().getRootElement().getChildren("blitz")) {
            if (result == null) result = TimeLimit.Result.MOST_PLAYERS;
            if (blitz.getChild("time") != null) timeLimit = Strings.timeStringToSeconds(blitz.getChild("time").getText());
        }
        if (timeLimit < 0) {
            timeLimit = 0;
        }
        if (result == null && GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class).size() > 0) {
            result = TimeLimit.Result.MOST_OBJECTIVES;
        }
        if (result == null) result = TimeLimit.Result.TIE;
        results.add(new TimeLimit(timeLimit, result, team));
        return results;
    }

}
