package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ProximityCommand {

    @Command(aliases = {"proximity"}, desc = "Shows the proximity of the objectives in the match.", usage = "[team]")
    public static void proximity(final CommandContext cmd, CommandSender sender) throws CommandException {
        Optional<TeamModule> team = Optional.absent();
        if (cmd.argsLength() > 0) {
            team = Teams.getTeamByName(cmd.getJoinedStrings(0));
            if (!team.isPresent())
                throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!(sender instanceof Player) || ObserverModule.testObserver((Player) sender) ||
                (sender.hasPermission("cardinal.proximity") || (team.isPresent() && team.get().contains(sender)))) {
            boolean objectives = false;
            if (team.isPresent() && hasObjectives(team.get())) {
                sendTeamInfo(team.get(), sender);
                objectives = true;
            } else if (!team.isPresent()) {
                for (TeamModule teams : Teams.getTeams()) {
                    if (!teams.isObserver() && hasObjectives(teams)) {
                        sendTeamInfo(teams, sender);
                        objectives = true;
                    }
                }
            }
            if (!objectives)
                sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_PROXIMITY_NO_SCORING).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        } else {
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_PROXIMITY_OBS_ONLY).getMessage(((Player) sender).getLocale()));
        }
    }

    private static boolean hasObjectives(TeamModule team) {
        if (Teams.getShownObjectives(team).size() > 0) return true;
        for (GameObjective objective : Teams.getShownSharedObjectives()) {
            if (!(objective instanceof HillObjective || (objective instanceof FlagObjective && ((FlagObjective) objective).isShared())))
                return true;
        }
        return false;
    }

    private static void sendTeamInfo(TeamModule team, CommandSender sender) {
        sender.sendMessage(team.getCompleteName());
        for (GameObjective objective : Teams.getShownObjectives(team))
            sender.sendMessage(getObjective(objective, objective.getProximityHandler(team)));
        for (GameObjective objective : Teams.getShownSharedObjectives()) {
            if (objective instanceof HillObjective || (objective instanceof FlagObjective && ((FlagObjective) objective).isShared())) continue;
            GameObjectiveProximityHandler proximityHandler = objective.getProximityHandler(team);
            if (proximityHandler != null)
                sender.sendMessage(getObjective(objective, proximityHandler));
        }
    }

    private static String getObjective(GameObjective objective, GameObjectiveProximityHandler proximityHandler) {
        String message = "  ";
        if (objective instanceof WoolObjective) message += MiscUtil.convertDyeColorToChatColor(((WoolObjective)objective).getColor());
        if (objective instanceof FlagObjective) message += ((FlagObjective) objective).getChatColor();
        message += WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + " ";
        message += objective.isComplete() ? ChatColor.GREEN + "COMPLETE " : objective.isTouched() ? ChatColor.YELLOW + "TOUCHED " : ChatColor.RED + "UNTOUCHED ";
        if (proximityHandler != null && !objective.isComplete()) {
            message += ChatColor.GRAY + proximityHandler.getProximityName() + ": ";
            message += ChatColor.AQUA + proximityHandler.getProximityAsString();
        }
        return message;
    }

}