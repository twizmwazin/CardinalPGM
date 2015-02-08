package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamUtils {

    public static TeamModule getTeamWithFewestPlayers(Match match) {
        TeamModule result = null;
        double percent = Double.POSITIVE_INFINITY;
        for (TeamModule team : getTeams()) {
            if (!team.isObserver() && (team.size() / (double) team.getMax()) < percent) {
                result = team;
                percent = team.size() / (double) team.getMax();
            }
        }
        return result;
    }

    public static TeamModule getTeamByName(String name) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getName().replaceAll(" ", "").toLowerCase().startsWith(name.replaceAll(" ", "").toLowerCase())) {
                return team;
            }
        }
        return null;
    }

    public static TeamModule getTeamById(String id) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "").equalsIgnoreCase(id.replaceAll(" ", ""))) {
                return team;
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "").toLowerCase().startsWith(id.replaceAll(" ", "").toLowerCase())) {
                return team;
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "-").equalsIgnoreCase(id.replaceAll(" ", "-"))) {
                return team;
            }
        }
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.getId().replaceAll(" ", "-").toLowerCase().startsWith(id.replaceAll(" ", "-").toLowerCase())) {
                return team;
            }
        }
        return null;
    }

    public static TeamModule getTeamByPlayer(Player player) {
        for (TeamModule team : GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class)) {
            if (team.contains(player)) {
                return team;
            }
        }
        return null;
    }

    public static ModuleCollection<TeamModule> getTeams() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(TeamModule.class);
    }

    public static ModuleCollection<GameObjective> getObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective instanceof WoolObjective) {
                if (objective.getTeam() == team) {
                    objectives.add(objective);
                }
            } else if (objective.getTeam() != team && !(objective instanceof HillObjective)) {
                objectives.add(objective);
            }
        }
        return objectives;
    }

    public static ModuleCollection<GameObjective> getShownObjectives(TeamModule team) {
        ModuleCollection<GameObjective> objectives = new ModuleCollection<>();
        for (GameObjective objective : getObjectives(team)) {
            if (objective.showOnScoreboard()) {
                objectives.add(objective);
            }
        }
        return objectives;
    }
}
