package in.twizmwaz.cardinal.match.scoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.teams.PgmTeam;
import org.bukkit.scoreboard.Team;

/**
 * Created by Connor on 12/23/2014.
 */
public class PgmScoreboard {
    public static void refresh(PgmTeam team) {
        // ...
    }

    public static void refreshAll() {
        for (PgmTeam team : GameHandler.getGameHandler().getMatch().getTeams()) {
            refresh(team);
        }
    }

    public static String getMiddlePart(String string) {
        if (string.length() > 48) {
            string = string.substring(0, 48);
        }
        if (string.length() <= 16) {
            return string;
        } else if (string.length() <= 32) {
            return string.substring(16);
        } else if (string.length() <= 48) {
            return string.substring(16, 32);
        }
        return null;
    }

    public static String convertToTeamParts(Team team, String string) {
        if (string.length() > 48) {
            string = string.substring(0, 48);
        }
        if (string.length() <= 16) {
            team.setPrefix("");
            team.add(string);
            team.setSuffix("");
            return string;
        } else if (string.length() <= 32) {
            team.setPrefix(string.substring(0, 16));
            team.add(string.substring(16));
            team.setSuffix("");
            return string.substring(16);
        } else if (string.length() <= 48) {
            team.setPrefix(string.substring(0, 16));
            team.add(string.substring(16, 32));
            team.setSuffix(string.substring(32));
            return string.substring(16, 32);
        }
        return null;
    }
}
