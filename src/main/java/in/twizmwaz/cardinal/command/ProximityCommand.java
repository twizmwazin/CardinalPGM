package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.cores.CoreObjective;
import in.twizmwaz.cardinal.module.modules.destroyable.DestroyableObjective;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.MiscUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProximityCommand {

    @Command(aliases = {"proximity"}, desc = "Shows the proximity of the objectives in the match.")
    public static void proximity(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player) || TeamUtils.getTeamByPlayer((Player) sender) != null || TeamUtils.getTeamByPlayer((Player) sender).isObserver() || !GameHandler.getGameHandler().getMatch().isRunning() || sender.hasPermission("cardinal.proximity")) {
            for (TeamModule team : TeamUtils.getTeams()) {
                if (!team.isObserver()) {
                    sender.sendMessage(team.getCompleteName());
                    for (GameObjective objective : TeamUtils.getShownObjectives(team)) {
                        if (objective.isComplete()) {
                            if (objective instanceof WoolObjective) {
                                WoolObjective wool = (WoolObjective) objective;
                                sender.sendMessage("  " + MiscUtils.convertDyeColorToChatColor(wool.getColor()) + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  " + ChatColor.GREEN + "COMPLETE");
                            } else {
                                sender.sendMessage("  " + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  " + ChatColor.GREEN + "COMPLETE");
                            }
                        } else if (objective.isTouched()) {
                            if (objective instanceof WoolObjective) {
                                WoolObjective wool = (WoolObjective) objective;
                                double proximity = ((WoolObjective) objective).getProximity();
                                sender.sendMessage("  " + MiscUtils.convertDyeColorToChatColor(wool.getColor()) + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  " + ChatColor.YELLOW + "TOUCHED" + ChatColor.GRAY + "  closest safety: " + ChatColor.AQUA + (proximity == Double.POSITIVE_INFINITY ? "Infinity" : (Math.round(proximity * 100.0) / 100.0)));
                            } else {
                                sender.sendMessage("  " + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  " + ChatColor.YELLOW + "TOUCHED");
                            }
                        } else {
                            if (objective instanceof WoolObjective) {
                                WoolObjective wool = (WoolObjective) objective;
                                double proximity = ((WoolObjective) objective).getProximity();
                                sender.sendMessage("  " + MiscUtils.convertDyeColorToChatColor(wool.getColor()) + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  " + ChatColor.RED + "UNTOUCHED" + ChatColor.GRAY + "  closest kill: " + ChatColor.AQUA + (proximity == Double.POSITIVE_INFINITY ? "Infinity" : (Math.round(proximity * 100.0) / 100.0)));
                            } else {
                                double proximity = objective instanceof DestroyableObjective ? ((DestroyableObjective) objective).getProximity() : objective instanceof CoreObjective ? ((CoreObjective) objective).getProximity() : 0.0;
                                sender.sendMessage("  " + WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + "  " + ChatColor.RED + "UNTOUCHED" + ChatColor.GRAY + "  closest player: " + ChatColor.AQUA + (proximity == Double.POSITIVE_INFINITY ? "Infinity" : (Math.round(proximity * 100.0) / 100.0)));
                            }
                        }
                    }
                }
            }
        } else throw new CommandPermissionsException();
    }

}