package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.NumUtils;
import in.twizmwaz.cardinal.util.StringUtils;
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
        String prefix;
        if (objective instanceof WoolObjective) {
            WoolObjective wool = (WoolObjective) objective;
            if (wool.isComplete()) {
                prefix = MiscUtils.convertDyeColorToChatColor(wool.getColor()) + " \u2B1B ";
            } else if (wool.isTouched() && (this.team == team || team.isObserver())) {
                prefix = MiscUtils.convertDyeColorToChatColor(wool.getColor()) + " \u2592 " + (wool.showProximity() ? ChatColor.RESET + NumUtils.convertToSubscript(wool.getProximity() == Double.POSITIVE_INFINITY || wool.getProximity() == Double.NEGATIVE_INFINITY ? wool.getProximity() : Math.round(wool.getProximity() * 10.0) / 10.0) + " " : "");
            } else if (this.team == team || team.isObserver()) {
                prefix = MiscUtils.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C " + (wool.showProximity() ? ChatColor.RESET + NumUtils.convertToSubscript(wool.getProximity() == Double.POSITIVE_INFINITY || wool.getProximity() == Double.NEGATIVE_INFINITY ? wool.getProximity() : Math.round(wool.getProximity() * 10.0) / 10.0) + " " : "");
            } else {
                prefix = MiscUtils.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C ";
            }
        } else if (objective instanceof CoreObjective) {
            CoreObjective core = (CoreObjective) objective;
            if (core.isComplete()) {
                prefix = ChatColor.GREEN + " \u2714 ";
            } else if (core.isTouched() && this.team != team) {
                prefix = ChatColor.YELLOW + " \u2733 ";
            } else if (this.team != team) {
                prefix = ChatColor.RED + " \u2715 " + ChatColor.RESET + (core.showProximity() ? NumUtils.convertToSubscript(core.getProximity() == Double.POSITIVE_INFINITY || core.getProximity() == Double.NEGATIVE_INFINITY ? core.getProximity() : Math.round(core.getProximity() * 10.0) / 10.0) + " " : "");
            } else {
                prefix = ChatColor.RED + " \u2715 ";
            }
        } else if (objective instanceof DestroyableObjective) {
            DestroyableObjective destroyable = (DestroyableObjective) objective;
            if (destroyable.showPercent()) {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " " + destroyable.getPercent() + "% ";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% ";
                } else if (this.team != team) {
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "% " + ChatColor.RESET + (destroyable.showProximity() ? NumUtils.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0) + " " : "");
                } else {
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "% ";
                }
            } else if (team.isObserver()) {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired() + " ";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired() + " ";
                } else if (this.team != team) {
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired() + " " + ChatColor.RESET + (destroyable.showProximity() ? NumUtils.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0) + " " : "");
                } else {
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired() + " ";
                }
            } else {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " \u2714 ";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " \u2733 ";
                } else if (this.team != team) {
                    prefix = ChatColor.RED + " \u2715 " + ChatColor.RESET + (destroyable.showProximity() ? NumUtils.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0) + " " : "");
                } else {
                    prefix = ChatColor.RED + " \u2715 ";
                }
            }
        } else if (objective instanceof HillObjective) {
            HillObjective hill = (HillObjective) objective;
            if (hill.isComplete()) {
                prefix = ChatColor.RESET + " \u29BF" + (hill.getTeam() != null ? hill.getTeam().getColor() : "") + " ";
            } else if (hill.isTouched()) {
                if (hill.showProgress()) {
                    prefix = ChatColor.RESET + " " + (hill.getCapturingTeam() != null ? hill.getCapturingTeam().getColor() : ChatColor.RESET) + "" + hill.getPercent() + "%" + (hill.getTeam() != null ? hill.getTeam().getColor() : "") + " ";
                } else {
                    prefix = ChatColor.RESET + " \u29BF" + (hill.getTeam() != null ? hill.getTeam().getColor() : "") + " ";
                }
            } else {
                prefix = ChatColor.RESET + " \u29BE ";
            }
        } else {
            prefix = " ";
        }
        while (prefix.length() > 16) {
            prefix = StringUtils.removeLastWord(prefix);
        }
        return prefix;
    }


}
