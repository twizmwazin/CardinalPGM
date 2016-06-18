package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.GameObjective;
import in.twizmwaz.cardinal.module.modules.ctf.FlagObjective;
import in.twizmwaz.cardinal.module.modules.hill.HillObjective;
import in.twizmwaz.cardinal.module.modules.proximity.GameObjectiveProximityHandler;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.wools.WoolObjective;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ProximityCommand {

    @Command(aliases = {"proximity"}, desc = "Shows the proximity of the objectives in the match.")
    public static void proximity(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player) || (Teams.getTeamByPlayer((Player) sender).isPresent() && Teams.getTeamByPlayer((Player) sender).get().isObserver()) ||
                !GameHandler.getGameHandler().getMatch().getState().equals(MatchState.PLAYING) || sender.hasPermission("cardinal.proximity")){
            boolean hasObjectives = false;
            for (GameObjective obj : GameHandler.getGameHandler().getMatch().getModules().getModules(GameObjective.class)) {
                if (obj.showOnScoreboard() && !(obj instanceof HillObjective) && !(obj instanceof FlagObjective && ((FlagObjective) obj).isShared())) {
                    hasObjectives = true;
                }
            }
            if (hasObjectives){
                for (TeamModule team : Teams.getTeams()) {
                    if (!team.isObserver()) {
                        sender.sendMessage(team.getCompleteName());
                        for (GameObjective objective : Teams.getShownObjectives(team)) {
                            GameObjectiveProximityHandler proximityHandler = objective.getProximityHandler(team);
                            String message = "  ";
                            if (objective instanceof WoolObjective) message += MiscUtil.convertDyeColorToChatColor(((WoolObjective)objective).getColor());
                            message += WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + " ";
                            message += objective.isComplete() ? ChatColor.GREEN + "COMPLETE " : objective.isTouched() ? ChatColor.YELLOW + "TOUCHED " : ChatColor.RED + "UNTOUCHED ";
                            if (proximityHandler != null) {
                                message += ChatColor.GRAY + proximityHandler.getProximityName() + ": ";
                                message += ChatColor.AQUA + proximityHandler.getProximityAsString();
                            }
                            sender.sendMessage(message);
                        }
                        for (GameObjective objective : Teams.getShownSharedObjectives()) {
                            if (objective instanceof HillObjective || (objective instanceof FlagObjective && ((FlagObjective) objective).isShared())) continue;
                            GameObjectiveProximityHandler proximityHandler = objective.getProximityHandler(team);
                            if (proximityHandler != null) {
                                String message = "  ";
                                if (objective instanceof WoolObjective) message += MiscUtil.convertDyeColorToChatColor(((WoolObjective)objective).getColor());
                                message += WordUtils.capitalizeFully(objective.getName().replaceAll("_", " ")) + " ";
                                message += objective.isComplete() ? ChatColor.GREEN + "COMPLETE " : objective.isTouched() ? ChatColor.YELLOW + "TOUCHED " : ChatColor.RED + "UNTOUCHED ";
                                message += ChatColor.GRAY + proximityHandler.getProximityName() + ": ";
                                message += ChatColor.AQUA + proximityHandler.getProximityAsString();
                                sender.sendMessage(message);
                            }

                        }
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_PROXIMITY_NO_SCORING).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
            }
        } else {
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_PROXIMITY_OBS_ONLY).getMessage(((Player) sender).getLocale()));
        }
    }
}