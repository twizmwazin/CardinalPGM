package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import org.bukkit.scoreboard.Team;

public class ScoreboardUtils {

    public static String getConversion(String string, String insertColor, boolean doNotInterruptColorCodes) {
        int max1 = 16 - insertColor.length();
        int max2 = 32 - (insertColor.length() * 2);
        int max3 = 48 - (insertColor.length() * 2);
        if (string.length() > max1) {
            if (string.substring(max1 - 1, max1).equals("§") && doNotInterruptColorCodes && !insertColor.equals("")) {
                max1--;
                max2--;
                max3--;
            }
        }
        if (string.length() > max3) {
            string = string.substring(0, max3);
        }
        if (string.length() <= max1) {
            return insertColor + string;
        } else if (string.length() <= max2) {
            return insertColor + string.substring(max1);
        } else if (string.length() <= max3) {
            return insertColor + string.substring(max1, max2);
        }
        return null;
    }

    public static String getConversion(String string, String insertColor) {
        return getConversion(string, insertColor, true);
    }

    public static String convertToScoreboard(Team team, String string, String insertColor) {
        return convertToScoreboard(team, string, insertColor, true);
    }

    public static String convertToScoreboard(Team team, String string, String insertColor, boolean doNotInterruptColorCodes) {
        int max1 = 16 - insertColor.length();
        int max2 = 32 - (insertColor.length() * 2);
        int max3 = 48 - (insertColor.length() * 2);
        if (string.length() > max1) {
            if (string.substring(max1 - 1, max1).equals("§") && doNotInterruptColorCodes && !insertColor.equals("")) {
                max1--;
                max2--;
                max3--;
            }
        }
        if (string.length() > max3) {
            string = string.substring(0, max3);
        }
        if (string.length() <= max1) {
            team.setPrefix("");
            team.add(insertColor + string);
            team.setSuffix("");
            return insertColor + string;
        } else if (string.length() <= max2) {
            team.setPrefix(insertColor + string.substring(0, max1));
            team.add(insertColor + string.substring(max1));
            team.setSuffix("");
            return insertColor + string.substring(max1);
        } else if (string.length() <= max3) {
            team.setPrefix(insertColor + string.substring(0, max1));
            team.add(insertColor + string.substring(max1, max2));
            team.setSuffix(string.substring(max2));
            return insertColor + string.substring(max1, max2);
        }
        return null;
    }

    public static ModuleCollection<HillObjective> getHills() {
        ModuleCollection<HillObjective> objectives = new ModuleCollection<>();
        for (HillObjective hill : GameHandler.getGameHandler().getMatch().getModules().getModules(HillObjective.class)) {
            objectives.add(hill);
        }
        return objectives;
    }

}
