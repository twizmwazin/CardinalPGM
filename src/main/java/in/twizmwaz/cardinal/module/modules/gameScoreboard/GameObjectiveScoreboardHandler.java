package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import org.bukkit.ChatColor;

public class GameObjectiveScoreboardHandler {

    private GameObjective objective;
    private TeamModule team;

    private static int num = 1;
    private int number;

    public GameObjectiveScoreboardHandler(GameObjective objective) {
        this.objective = objective;
        this.team = objective.getTeam();
        this.number = num;
        num ++;
    }

    public int getNumber() {
        return number;
    }

    public String getPrefix(TeamModule team) {
        if (objective instanceof WoolObjective) {
            WoolObjective wool = (WoolObjective) objective;
            if (wool.isComplete()) {
                return StringUtils.convertDyeColorToChatColor(wool.getColor()) + "\u2B1B";
            } else if (wool.isTouched() && this.team == team) {
                return StringUtils.convertDyeColorToChatColor(wool.getColor()) + "\u2592";
            } else {
                return StringUtils.convertDyeColorToChatColor(wool.getColor()) + "\u2B1C";
            }
        } else if (objective instanceof CoreObjective) {
            CoreObjective core = (CoreObjective) objective;
            if (core.isComplete()) {
                return ChatColor.GREEN + "\u2714";
            } else if (core.isTouched() && this.team != team) {
                return ChatColor.YELLOW + "\u2733";
            } else {
                return ChatColor.RED + "\u2715";
            }
        } else if (objective instanceof DestroyableObjective) {
            DestroyableObjective destroyable = (DestroyableObjective) objective;
            if (destroyable.showPercent()) {
                if (destroyable.isComplete()) {
                    return ChatColor.GREEN + "" + destroyable.getPercent() + "%";
                } else if (destroyable.isTouched() && this.team != team) {
                    return ChatColor.YELLOW + "" + destroyable.getPercent() + "%";
                } else {
                    return ChatColor.RED + "" + destroyable.getPercent() + "%";
                }
            } else {
                if (destroyable.isComplete()) {
                    return ChatColor.GREEN + "\u2714";
                } else if (destroyable.isTouched() && this.team != team) {
                    return ChatColor.YELLOW + "\u2733";
                } else {
                    return ChatColor.RED + "\u2715";
                }
            }
        }
        return "";
    }
}
