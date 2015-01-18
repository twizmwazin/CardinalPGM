package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamUtils {

    public static TeamModule getTeamWithFewestPlayers(Match match) {
        TeamModule result = null;
        List<Integer> teamValues = new ArrayList<>();
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (!team.isObserver()) {
                teamValues.add(team.size());
            }
        }
        Collections.sort(teamValues);
        for (TeamModule team : match.getModules().getModules(TeamModule.class)) {
            if (team.size() == teamValues.get(0) && !team.isObserver()) {
                result = team;
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

    public static Set<GameObjective> getObjectives(TeamModule team) {
        Set<GameObjective> objectives = new HashSet<>();
        for (GameObjective objective : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
            if (objective.getTeam() == team) {
                objectives.add(objective);
            }
        }
        return objectives;
    }
}
