package in.twizmwaz.cardinal.module.modules.scoreboard;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Numbers;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.Teams;
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
        String prefix = "";
        if (objective instanceof WoolObjective) {
            WoolObjective wool = (WoolObjective) objective;
            if (wool.isComplete()) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1B";
            } else if (wool.isTouched() && (this.team == team || team.isObserver())) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2592 " + (wool.showProximity() ? asProximitySubscript(true, wool.getProximity()) : "");
            } else if (this.team == team || team.isObserver()) {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C " + (wool.showProximity() ? asProximitySubscript(false, wool.getProximity()) : "");
            } else {
                prefix = MiscUtil.convertDyeColorToChatColor(wool.getColor()) + " \u2B1C";
            }
        } else if (objective instanceof CoreObjective) {
            CoreObjective core = (CoreObjective) objective;
            if (core.isComplete()) {
                prefix = ChatColor.GREEN + " \u2714";
            } else if (core.isTouched() && this.team != team) {
                prefix = ChatColor.YELLOW + " \u2733";
            } else {
                Double proximity = core.getProximity(team);
                String proximity2 = proximity != null && core.showProximity(team) ? asProximitySubscript(false, proximity) : "";
                prefix = ChatColor.RED + " \u2715 " + ChatColor.GRAY + proximity2;
            }
        } else if (objective instanceof DestroyableObjective) {
            DestroyableObjective destroyable = (DestroyableObjective) objective;
            if (team.isObserver()) {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired();
                } else if (destroyable.isTouched()) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired();
                } else {
                    Double proximity = destroyable.getProximity(team);
                    String proximity2 = proximity != null && destroyable.showProximity(team) ? asProximitySubscript(false, proximity) : "";
                    prefix = " " + ChatColor.RED + destroyable.getPercent() + "% " + ChatColor.GRAY + destroyable.getBlocksBroken() + "/" + destroyable.getBlocksRequired() + " " + proximity2;
                    if (proximity != null && destroyable.showProximity(team) && prefix.length() > 16) {
                        prefix = " " + prefix.split(" ")[1] + " " + ChatColor.GRAY  + prefix.split(" ")[3];
                    }
                }
            } else if (destroyable.showPercent()) {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " " + destroyable.getPercent() + "%";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " " + destroyable.getPercent() + "% ";
                } else {
                    Double proximity = destroyable.getProximity(team);
                    String proximity2 = proximity != null && destroyable.showProximity(team) ? asProximitySubscript(false, proximity) : "";
                    prefix = ChatColor.RED + " " + destroyable.getPercent() + "% " + ChatColor.GRAY + proximity2;
                }
            } else {
                if (destroyable.isComplete()) {
                    prefix = ChatColor.GREEN + " \u2714";
                } else if (destroyable.isTouched() && this.team != team) {
                    prefix = ChatColor.YELLOW + " \u2733";
                } else {
                    Double proximity = destroyable.getProximity(team);
                    String proximity2 = proximity != null && destroyable.showProximity(team) ? asProximitySubscript(false, proximity) : "";
                    prefix = ChatColor.RED + " \u2715 " + ChatColor.GRAY + proximity2;
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
        } else if (objective instanceof FlagObjective) {
            FlagObjective flagObjective = (FlagObjective) objective;
            ChatColor color = flagObjective.isRespawning() ? ChatColor.GRAY : (flagObjective.isShared() ? ChatColor.WHITE : flagObjective.getChatColor());
            prefix = color + (flagObjective.isOnPost() ? " \u2691 " : " \u2690 ");
            Double proximity = flagObjective.getProximity(team);
            String proximity2 = proximity != null && flagObjective.showProximity(team) ? asProximitySubscript(flagObjective.isTouched() , proximity) : "";
            if (!GameHandler.getGameHandler().getMatch().isRunning()) return prefix + proximity2;
            if (flagObjective.isRespawning() && flagObjective.getRespawnTime() > 0) {
                prefix = ChatColor.GRAY + " " + flagObjective.getRespawnTime() + " ";
            } else if (flagObjective.isDropped() && flagObjective.getRecoverTime() > 0) {
                prefix = ChatColor.AQUA + " " + flagObjective.getRecoverTime() + " ";
            } else if (flagObjective.isCarried()) {
                ChatColor teamColor = (System.currentTimeMillis() / 250) % 2 == 0 ? (flagObjective.isShared() ? Teams.getTeamColorByPlayer(flagObjective.getPicker()) : flagObjective.getChatColor()) : ChatColor.BLACK;
                prefix = teamColor + " \u2794 ";
            }
            prefix = prefix + proximity2;
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
        } else if (objective instanceof FlagObjective) {
            FlagObjective flagObjective = (FlagObjective) objective;
            if (flagObjective.isRespawning() && flagObjective.getRespawnTime() > 0) {
                prefix = ChatColor.GRAY + " " + flagObjective.getRespawnTime() + " ";
            } else if (flagObjective.isDropped() && flagObjective.getRecoverTime() > 0) {
                prefix = ChatColor.AQUA + " " + flagObjective.getRecoverTime() + " ";
            } else if (flagObjective.isCarried()) {
                ChatColor teamColor = (System.currentTimeMillis() / 250) % 2 == 0 ? (flagObjective.isShared() ? Teams.getTeamColorByPlayer(flagObjective.getPicker()) : flagObjective.getChatColor()) : ChatColor.BLACK;
                prefix = teamColor + " \u2794 ";
            } else {
                ChatColor color = flagObjective.isRespawning() ? ChatColor.GRAY : (flagObjective.isShared() ? ChatColor.WHITE : flagObjective.getChatColor());
                prefix = color + (flagObjective.isOnPost() ? " \u2691 " : " \u2690 ");
            }
        } else {
            prefix = "";
        }
        return prefix;
    }

    public String asProximitySubscript(boolean touch, Double d) {
        return (touch ? ChatColor.YELLOW : ChatColor.GRAY) + Numbers.convertToSubscript(d == Double.POSITIVE_INFINITY ? d : Math.round(d * 10.0) / 10.0);
    }

}
