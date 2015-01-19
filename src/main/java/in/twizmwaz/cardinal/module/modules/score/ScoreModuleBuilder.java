package in.twizmwaz.cardinal.module.modules.score;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.module.modules.gameScoreboard.GameScoreboard;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.regions.Region;
import in.twizmwaz.cardinal.util.StringUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jdom2.Element;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@BuilderData(load = ModuleLoadTime.LATER)
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
                max = Integer.parseInt(score.getChild("limit").getText());
                if (max != 0) {
                    pointsPerKill = 1;
                    pointsPerDeath = 1;
                }
            }
            if (score.getChild("time") != null) {
                try {
                    time = StringUtils.timeStringToSeconds(score.getChild("time").getText());
                } catch (ParseException e) {
                }
                if (time != 0) {
                    pointsPerKill = 1;
                    pointsPerDeath = 1;
                }
            } else {
                Bukkit.broadcastMessage("dx");
            }
            if (score.getChild("kills") != null) {
                pointsPerKill = Integer.parseInt(score.getChild("kills").getText());
            }
            if (score.getChild("deaths") != null) {
                pointsPerDeath = Integer.parseInt(score.getChild("deaths").getText());
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
