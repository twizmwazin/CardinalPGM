package in.twizmwaz.cardinal.module.modules.gameScoreboard;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.NumUtils;
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
            } else if (wool.isTouched() && (this.team == team || team.isObserver())) {
                return StringUtils.convertDyeColorToChatColor(wool.getColor()) + "\u2592 " + ChatColor.RESET + NumUtils.convertToSubscript(wool.getProximity() == Double.POSITIVE_INFINITY || wool.getProximity() == Double.NEGATIVE_INFINITY ? wool.getProximity() : Math.round(wool.getProximity() * 10.0) / 10.0);
            } else if (this.team == team || team.isObserver()) {
                return StringUtils.convertDyeColorToChatColor(wool.getColor()) + "\u2B1C " + ChatColor.RESET + NumUtils.convertToSubscript(wool.getProximity() == Double.POSITIVE_INFINITY || wool.getProximity() == Double.NEGATIVE_INFINITY ? wool.getProximity() : Math.round(wool.getProximity() * 10.0) / 10.0);
            } else {
                return StringUtils.convertDyeColorToChatColor(wool.getColor()) + "\u2B1C";
            }
        } else if (objective instanceof CoreObjective) {
            CoreObjective core = (CoreObjective) objective;
            if (core.isComplete()) {
                return ChatColor.GREEN + "\u2714";
            } else if (core.isTouched() && this.team != team) {
                return ChatColor.YELLOW + "\u2733";
            } else if (this.team != team) {
                return ChatColor.RED + "\u2715 " + ChatColor.RESET + NumUtils.convertToSubscript(core.getProximity() == Double.POSITIVE_INFINITY || core.getProximity() == Double.NEGATIVE_INFINITY ? core.getProximity() : Math.round(core.getProximity() * 10.0) / 10.0);
            } else {
                return ChatColor.RED + "\u2715";
            }
        } else if (objective instanceof DestroyableObjective) {
            DestroyableObjective destroyable = (DestroyableObjective) objective;
            if (destroyable.showPercent() || team.isObserver()) {
                if (destroyable.isComplete()) {
                    return ChatColor.GREEN + "" + destroyable.getPercent() + "%";
                } else if (destroyable.isTouched() && this.team != team) {
                    return ChatColor.YELLOW + "" + destroyable.getPercent() + "%";
                } else if (this.team != team) {
                    return ChatColor.RED + "" + destroyable.getPercent() + "% " + ChatColor.RESET + NumUtils.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0);
                } else {
                    return ChatColor.RED + "" + destroyable.getPercent() + "%";
                }
            } else {
                if (destroyable.isComplete()) {
                    return ChatColor.GREEN + "\u2714";
                } else if (destroyable.isTouched() && this.team != team) {
                    return ChatColor.YELLOW + "\u2733";
                } else if (this.team != team) {
                    return ChatColor.RED + "\u2715 " + ChatColor.RESET + NumUtils.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0);
                } else {
                    return ChatColor.RED + "\u2715";
                }
            }
        } else if (objective instanceof HillObjective) {
            HillObjective hill = (HillObjective) objective;
            if (hill.isComplete()) {
                return ChatColor.RESET + "" + "\u29BF" + (hill.getTeam() != null ? hill.getTeam().getColor() : ChatColor.RESET);
            } else if (hill.isTouched()) {
                if (hill.showProgress()) {
                    return ChatColor.RESET + "" + (hill.getCapturingTeam() != null ? hill.getCapturingTeam().getColor() : ChatColor.RESET) + "" + hill.getPercent() + "%" + (hill.getTeam() != null ? hill.getTeam().getColor() : ChatColor.RESET);
                } else {
                    return ChatColor.RESET + "" + "\u29BF" + (hill.getTeam() != null ? hill.getTeam().getColor() : ChatColor.RESET);
                }
            } else {
                return ChatColor.RESET + "" + "\u29BE";
            }

        }
        return "";
    }
}
