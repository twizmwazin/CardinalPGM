package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.ChatColor;

public class GameObjectiveScoreboardHandler {

    private static int num = 1;
    private GameObjective objective;
    private TeamModule team;
    private int number;

    public GameObjectiveScoreboardHandler(GameObjective objective) {
        this.objective = objective;
        this.team = objective.getTeam();
        this.number = num;
        num++;
    }

    public int getNumber() {
        return number;
    }

    public String getPrefix(TeamModule team) {
        String prefix;
        if (objective instanceof WoolObjective) {
            WoolObjective wool = (WoolObjective) objective;
            if (wool.isComplete()) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1B";
            } else if (wool.isTouched() && (this.team == team || team.isObserver())) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2592 " + (wool.showProximity() ? ChatColor.YELLOW + Numbers.convertToSubscript(wool.getProximity() == Double.POSITIVE_INFINITY || wool.getProximity() == Double.NEGATIVE_INFINITY ? wool.getProximity() : Math.round(wool.getProximity() * 10.0) / 10.0) : "");
            } else if (this.team == team || team.isObserver()) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C " + (wool.showProximity() ? ChatColor.GRAY + Numbers.convertToSubscript(wool.getProximity() == Double.POSITIVE_INFINITY || wool.getProximity() == Double.NEGATIVE_INFINITY ? wool.getProximity() : Math.round(wool.getProximity() * 10.0) / 10.0) : "");
            } else {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C";
            }
        } else if (objective instanceof CoreObjective) {
            CoreObjective core = (CoreObjective) objective;
            if (core.isComplete()) {
                prefix = ChatColor.GREEN + " \u2714";
            } else if (core.isTouched() && this.team != team) {
                prefix = ChatColor.YELLOW + " \u2733";
            } else if (this.team != team) {
                prefix = ChatColor.RED + " \u2715 " + ChatColor.GRAY + (core.showProximity() ? Numbers.convertToSubscript(core.getProximity() == Double.POSITIVE_INFINITY || core.getProximity() == Double.NEGATIVE_INFINITY ? core.getProximity() : Math.round(core.getProximity() * 10.0) / 10.0) : "");
            } else {
                prefix = ChatColor.RED + " \u2715";
            }
        } else if (objective instanceof DestroyableObjective) {
            DestroyableObjective destroyable = (DestroyableObjective) objective;
            if (destroyable.showPercent()) {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " " + destroyable.getPercent() + "%";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% ";
                } else if (this.team != team) {
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + (destroyable.showProximity() ? Numbers.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0) : "");
                } else {
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "%";
                }
            } else if (team.isObserver()) {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired();
                } else if (destroyable.isTouched()) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired();
                } else {
                    prefix = " " + ChatColor.RED + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired() + (destroyable.showProximity() ? " " + Numbers.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0) : "");
                    if (destroyable.showProximity() && prefix.length() > 16) {
                        prefix = " " + prefix.split(" ")[1] + " " +ChatColor.GRAY  + prefix.split(" ")[3];
                    }
                }
            } else {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " \u2714";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " \u2733";
                } else if (this.team != team) {
                    prefix = ChatColor.RED + " \u2715 " + ChatColor.GRAY + (destroyable.showProximity() ? Numbers.convertToSubscript(destroyable.getProximity() == Double.POSITIVE_INFINITY || destroyable.getProximity() == Double.NEGATIVE_INFINITY ? destroyable.getProximity() : Math.round(destroyable.getProximity() * 10.0) / 10.0) : "");
                } else {
                    prefix = ChatColor.RED + " \u2715";
                }
            }
        } else if (objective instanceof HillObjective) {
            HillObjective hill = (HillObjective) objective;
            if (hill.isComplete()) {
                prefix = ChatColor.RESET + " \u29BF" + (hill.getTeam() != null ? hill.getTeam().getColor() : "");
            } else if (hill.isTouched()) {
                if (hill.showProgress()) {
                    prefix = (hill.getCapturingTeam() != null ? hill.getCapturingTeam().getColor() : ChatColor.RESET) + " " + hill.getPercent() + "%" + (hill.getTeam() != null ? hill.getTeam().getColor() : ChatColor.RESET);
                } else {
                    prefix = ChatColor.RESET + " \u29BF" + (hill.getTeam() != null ? hill.getTeam().getColor() : "");
                }
            } else {
                prefix = ChatColor.RESET + " \u29BE";
            }
        } else {
            prefix = " ";
        }
        while (prefix.length() > 16) {
            prefix = Strings.removeLastWord(prefix);
        }
        return prefix;
    }

    public String getCompactPrefix(TeamModule team) {
        String prefix;
        if (objective instanceof WoolObjective) {
            WoolObjective wool = (WoolObjective) objective;
            if (wool.isComplete()) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1B ";
            } else if (wool.isTouched() && (this.team == team || team.isObserver())) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2592 ";
            } else {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C ";
            }
        } else if (objective instanceof CoreObjective) {
            CoreObjective core = (CoreObjective) objective;
            if (core.isComplete()) {
                prefix = ChatColor.GREEN + " \u2714 ";
            } else if (core.isTouched() && this.team != team) {
                prefix = ChatColor.YELLOW + " \u2733 ";
            } else {
                prefix = ChatColor.RED + " \u2715 ";
            }
        } else if (objective instanceof DestroyableObjective) {
            DestroyableObjective destroyable = (DestroyableObjective) objective;
            if (destroyable.isComplete()) {
                prefix = ChatColor.GREEN + " \u2714 ";
            } else if (destroyable.isTouched()) {
                if (team.isObserver()) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% ";
                } else if (destroyable.showPercent()) {
                    if (this.team == team) {
                        prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% ";
                    } else {
                        prefix = ChatColor.RED + " " + destroyable.getPercent() + "% ";
                    }
                } else {
                    if (this.team == team) {
                        prefix = ChatColor.YELLOW + " \u2733 ";
                    } else {
                        prefix = ChatColor.RED + " \u2715 ";
                    }
                }
            } else {
                prefix = ChatColor.RED + " \u2715 ";
            }
        } else {
            prefix = "";
        }
        return prefix;
    }

}
