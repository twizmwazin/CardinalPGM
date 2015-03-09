package in.twizmwaz.cardinal.module.modules.timeLimit;

import in.twizmwaz.cardinal.match.Match;
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
        for (Element time : match.getDocument().getRootElement().getChildren("time")) {
            int timeLimit = StringUtils.timeStringToSeconds(time.getText());
            if (timeLimit < 0) timeLimit = 0;
            TimeLimit.Result result = TimeLimit.Result.TIE;
            TeamModule winner = null;
            if (time.getAttributeValue("result") != null) {
                if (time.getAttributeValue("result").equalsIgnoreCase("objectives")) {
                    result = TimeLimit.Result.OBJECTIVES;
                } else if (time.getAttributeValue("result").equalsIgnoreCase("tie")) {
                    result = TimeLimit.Result.TIE;
                } else {
                    result = TimeLimit.Result.TEAM;
                    winner = TeamUtils.getTeamById(time.getAttributeValue("result"));
                }
            }
            results.add(new TimeLimit(timeLimit, result, winner));
        }
        return results;
    }

}
