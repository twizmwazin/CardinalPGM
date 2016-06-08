package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.matchTimer.MatchTimer;
import in.twizmwaz.cardinal.module.modules.monumentModes.MonumentModes;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.MiscUtil;
import in.twizmwaz.cardinal.util.Strings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModesCommand {

    @Command(aliases = {"modes", "mode"}, desc = "Lists information about the map's monument modes.", usage = "[list, next, push] [page]", max = 2)
    public static void modes(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(MonumentModes.class) == null) {
            throw new CommandException(ChatConstant.ERROR_NO_RESULT_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
            ModuleCollection<MonumentModes> modes = GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class);
            Map<MonumentModes, Integer> modesWithTime = new HashMap<>();
            for (MonumentModes modeForTime : modes) {
                modesWithTime.put(modeForTime, modeForTime.getTimeAfter());
            }
            List<MonumentModes> sortedModes = MiscUtil.getSortedHashMapKeyset(modesWithTime);
            Collections.reverse(sortedModes);
            if (cmd.argsLength() == 0) {
                int page = 1;
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "{0}" + ChatColor.DARK_AQUA + " (" + ChatColor.DARK_AQUA + "{1}" + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------", ChatConstant.GENERIC_MONUMENT_MODES.asMessage(), new LocalizedChatMessage(ChatConstant.UI_OF, ChatColor.AQUA + "" + page + ChatColor.DARK_AQUA, ChatColor.AQUA + "" + ((modes.size() + 7) / 8))).getMessage(ChatUtil.getLocale(sender)));
                int count = 1;
                for (MonumentModes mode : sortedModes) {
                    if ((count + 7) / 8 == page) {
                        sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + Strings.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + Strings.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                    }
                    count++;
                }
            } else if (cmd.argsLength() == 1) {
                if (cmd.getString(0).equalsIgnoreCase("list")) {
                    int page = 1;
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "{0}" + ChatColor.DARK_AQUA + " (" + ChatColor.DARK_AQUA + "{1}" + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------", ChatConstant.GENERIC_MONUMENT_MODES.asMessage(), new LocalizedChatMessage(ChatConstant.UI_OF, ChatColor.AQUA + "" + page + ChatColor.DARK_AQUA, ChatColor.AQUA + "" + ((modes.size() + 7) / 8))).getMessage(ChatUtil.getLocale(sender)));
                    int count = 1;
                    for (MonumentModes mode : sortedModes) {
                        if ((count + 7) / 8 == page) {
                            sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + Strings.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + Strings.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                        }
                        count++;
                    }
                } else if (cmd.getString(0).equalsIgnoreCase("next")) {
                    for (MonumentModes mode : sortedModes) {
                        if (!mode.hasRan()) {
                            sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_NEXT_MODE, ChatColor.GOLD + mode.getType().name().replaceAll("_", " ") + ChatColor.AQUA, Strings.formatTime(mode.getTimeAfter() - MatchTimer.getTimeInSeconds())).getMessage(ChatUtil.getLocale(sender)));
                            return;
                        }
                    }
                    throw new CommandException(ChatConstant.ERROR_NO_RESULT_MATCH.getMessage(ChatUtil.getLocale(sender)));
                } else if (cmd.getString(0).equalsIgnoreCase("push")) {
                    if (!sender.hasPermission("cardinal.modes.push")) throw new CommandPermissionsException();
                    throw new CommandUsageException(ChatConstant.ERROR_TOO_FEW_ARGUMENTS.getMessage(ChatUtil.getLocale(sender)), "/modes push <time period>");
                } else {
                    int page = cmd.getInteger(1);
                    if (page <= ((modes.size() + 7) / 8)) {
                        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "{0}" + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + page + ChatColor.DARK_AQUA + " {1} " + ChatColor.AQUA + ((modes.size() + 7) / 8) + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------", ChatConstant.GENERIC_MONUMENT_MODES.asMessage(), ChatConstant.UI_OF.asMessage()).getMessage(ChatUtil.getLocale(sender)));
                        int count = 1;
                        for (MonumentModes mode : sortedModes) {
                            if ((count + 7) / 8 == page) {
                                sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + Strings.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + Strings.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                            }
                            count++;
                        }
                    } else {
                        throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_INVALID_PAGE_NUMBER, "" + ((modes.size() + 7) / 8)).getMessage(ChatUtil.getLocale(sender)));
                    }
                }
            } else {
                if (cmd.getString(0).equalsIgnoreCase("list")) {
                    int page = cmd.getInteger(1);
                    if (page <= ((modes.size() + 7) / 8)) {
                        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "-------------- " + ChatColor.RESET + "{0}" + ChatColor.DARK_AQUA + " (" + ChatColor.AQUA + page + ChatColor.DARK_AQUA + " {1} " + ChatColor.AQUA + ((GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class).size() + 7) / 8) + ChatColor.DARK_AQUA + ")" + ChatColor.RED + " --------------", ChatConstant.GENERIC_MONUMENT_MODES.asMessage(), ChatConstant.UI_OF.asMessage()).getMessage(ChatUtil.getLocale(sender)));
                        int count = 1;
                        for (MonumentModes mode : sortedModes) {
                            if ((count + 7) / 8 == page) {
                                sender.sendMessage(ChatColor.GOLD + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + count + ". " + ChatColor.LIGHT_PURPLE + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + mode.getType().name().replaceAll("_", " ") + " - " + ChatColor.AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + Strings.formatTime(mode.getTimeAfter()) + ChatColor.DARK_AQUA + (mode.hasRan() ? ChatColor.STRIKETHROUGH + "" : "") + (GameHandler.getGameHandler().getMatch().isRunning() ? " (" + Strings.formatTime((mode.getTimeAfter() - MatchTimer.getTimeInSeconds() < 0) ? 0 : mode.getTimeAfter() - MatchTimer.getTimeInSeconds()) + " left)" : ""));
                            }
                            count++;
                        }
                    } else {
                        throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_INVALID_PAGE_NUMBER, "" + ((modes.size() + 7) / 8)).getMessage(ChatUtil.getLocale(sender)));
                    }
                } else if (cmd.getString(0).equalsIgnoreCase("push")) {
                    if (!sender.hasPermission("cardinal.modes.push")) {
                        throw new CommandPermissionsException();
                    }
                    int time;
                    try {
                        time = Strings.timeStringToSeconds(cmd.getString(1));
                    } catch (NumberFormatException e) {
                        throw new CommandException(ChatConstant.ERROR_TIME_FORMAT_STRING.getMessage(ChatUtil.getLocale(sender)));
                    }
                    for (MonumentModes mode : GameHandler.getGameHandler().getMatch().getModules().getModules(MonumentModes.class)) {
                        mode.setTimeAfter(mode.getTimeAfter() + time);
                    }
                    sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(time < 0 ? ChatConstant.GENERIC_MODES_PUSHED_BACKWARDS : ChatConstant.GENERIC_MODES_PUSHED_FORWARDS, Strings.formatTime(time)).getMessage(ChatUtil.getLocale(sender)));
                }
            }
    }

}
