package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.blitz.Blitz;
import in.twizmwaz.cardinal.module.modules.score.ScoreModule;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.timeNotifications.TimeNotifications;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TimeLimitCommand {

    @Command(aliases = {"timelimit", "tl"}, desc = "Modify the time limit of the current match.")
    public static void timeLimit(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (cmd.argsLength() == 0) {
            if (GameHandler.getGameHandler().getMatch().getPriorityTimeLimit() != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}", "The time limit is", StringUtils.formatTimeWithMillis(GameHandler.getGameHandler().getMatch().getPriorityTimeLimit())).getMessage(ChatUtils.getLocale(sender)));
            } else {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", "There is no time limit.").getMessage(ChatUtils.getLocale(sender)));
            }
        } else {
            if (!sender.hasPermission("cardinal.timelimit")) throw new CommandPermissionsException();
            int time;
            try {
                time = StringUtils.timeStringToSeconds(cmd.getString(0));
            } catch (NumberFormatException e) {
                if (cmd.getString(0).equalsIgnoreCase("cancel")) {
                    time = 0;
                } else {
                    throw new CommandException("Time format expected, string received instead.");
                }
            }
            boolean set = false;
            if (TimeLimit.getMatchTimeLimit() != 0) {
                for (TimeLimit module : GameHandler.getGameHandler().getMatch().getModules().getModules(TimeLimit.class)) {
                    module.setTimeLimit(time);
                    set = true;
                }
            }
            if (ScoreModule.getTimeLimit() != 0) {
                for (ScoreModule module : GameHandler.getGameHandler().getMatch().getModules().getModules(ScoreModule.class)) {
                    module.setTimeLimit(time);
                    set = true;
                }
            }
            if (Blitz.getTimeLimit() != 0) {
                for (Blitz module : GameHandler.getGameHandler().getMatch().getModules().getModules(Blitz.class)) {
                    module.setTimeLimit(time);
                    set = true;
                }
            }
            if (!set) {
                GameHandler.getGameHandler().getMatch().getModules().getModule(TimeLimit.class).setTimeLimit(time);
            }
            TimeNotifications.resetNextMessage();
            if (GameHandler.getGameHandler().getMatch().getPriorityTimeLimit() != 0) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}" + " " + ChatColor.AQUA + "{1}", "The time limit is", StringUtils.formatTimeWithMillis(GameHandler.getGameHandler().getMatch().getPriorityTimeLimit())).getMessage(ChatUtils.getLocale(sender)));
            } else {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.YELLOW + "{0}", "Time limit cancelled").getMessage(ChatUtils.getLocale(sender)));
            }
        }
    }

}
