package in.twizmwaz.cardinal.module.modules.timeLimit;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.jdom2.Element;

public class TimeLimitBuilder implements ModuleBuilder {

    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection<TimeLimit> results = new ModuleCollection<>();
        int timeLimit = 0;
        TimeLimit.Result result = null;
        TeamModule team = null;
        for (Element time : match.getDocument().getRootElement().getChildren("time")) {
            timeLimit = StringUtils.timeStringToSeconds(time.getText());
            result = TimeLimit.Result.TIE;
            if (time.getAttributeValue("result") != null) {
                if (time.getAttributeValue("result").equalsIgnoreCase("objectives")) {
                    result = TimeLimit.Result.MOST_OBJECTIVES;
                } else if (time.getAttributeValue("result").equalsIgnoreCase("tie")) {
                    result = TimeLimit.Result.TIE;
                } else {
                    result = TimeLimit.Result.TEAM;
                    team = TeamUtils.getTeamById(time.getAttributeValue("result")).orNull();
                }
            }
        }
        for (Element score : match.getDocument().getRootElement().getChildren("score")) {
            if (timeLimit <= 0) {
                result = TimeLimit.Result.HIGHEST_SCORE;
            }
            if (score.getChild("time") != null) {
                timeLimit = StringUtils.timeStringToSeconds(score.getChild("time").getText());
                result = TimeLimit.Result.HIGHEST_SCORE;
            }
        }
        for (Element blitz : match.getDocument().getRootElement().getChildren("blitz")) {
            if (timeLimit <= 0) {
                result = TimeLimit.Result.MOST_PLAYERS;
            }
            if (blitz.getChild("time") != null) {
                timeLimit = StringUtils.timeStringToSeconds(blitz.getChild("time").getText());
                result = TimeLimit.Result.MOST_PLAYERS;
            }
        }
        if (timeLimit < 0) {
            timeLimit = 0;
        }
        if (result == null && GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class).size() > 0) {
            result = TimeLimit.Result.MOST_OBJECTIVES;
        }
        results.add(new TimeLimit(timeLimit, result, team));
        return results;
    }

}
