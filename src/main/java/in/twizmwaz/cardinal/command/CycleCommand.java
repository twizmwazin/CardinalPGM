package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.modules.timeLimit.TimeLimit;
import in.twizmwaz.cardinal.module.modules.timers.Countdown;
import in.twizmwaz.cardinal.module.modules.timers.CycleTimer;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time] [map]", flags = "fn")
    @CommandPermissions("cardinal.match.cycle")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        processCycle(cmd, sender);
        if (cmd.argsLength() > 1) {
            LoadedMap next = getMap(cmd.getJoinedStrings(1).replace(" -f", "").replace("-f ", ""));
            if (next == null) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_MAP_MATCH).getMessage(ChatUtil.getLocale(sender)));
            } else {
                setCycleMap(next);
            }
        }
        GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimer.class).startCountdown(cmd.getInteger(0, Config.cycleDefault));
    }

    @Command(aliases = {"setnext", "sn"}, desc = "Sets the next map.", usage = "[map]", min = 1)
    @CommandPermissions("cardinal.match.setnext")
    public static void setNext(final CommandContext cmd, CommandSender sender) throws CommandException {
        LoadedMap nextMap = getMap(cmd.getJoinedStrings(0));
        if (nextMap == null) {
            throw new CommandException(ChatConstant.ERROR_NO_MAP_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        setCycleMap(nextMap);
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_SET, ChatColor.GOLD + nextMap.getName() + ChatColor.DARK_PURPLE).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"recycle", "rc"}, desc = "Cycles to the current map.", usage = "[time]", flags = "f")
    @CommandPermissions("cardinal.match.cycle")
    public static void recycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        processCycle(cmd, sender);
        setCycleMap(GameHandler.getGameHandler().getMatch().getLoadedMap());
        GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimer.class).startCountdown(cmd.getInteger(0, Config.cycleDefault));
    }

    private static LoadedMap getMap(String input) {
        input = input.toLowerCase().replaceAll(" ", "");
        LoadedMap result = null;
        for (LoadedMap loadedMap : GameHandler.getGameHandler().getRotation().getLoaded()) {
            if (loadedMap.getName().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(input.toLowerCase())) {
                result = loadedMap;
            }
        }
        if (result == null) {
            for (LoadedMap loadedMap : GameHandler.getGameHandler().getRotation().getLoaded()) {
                if (loadedMap.getName().toLowerCase().replaceAll(" ", "").startsWith(input.toLowerCase())) {
                    result = loadedMap;
                }
            }
        }
        return result;
    }

    private static void setCycleMap(LoadedMap map) {
        GameHandler.getGameHandler().getCycle().setMap(map);
    }

    private static void processCycle(CommandContext cmd, CommandSender sender) throws CommandException {
        Match match = GameHandler.getGameHandler().getMatch();
        if ((match.isRunning() || match.isStarting()) && !cmd.hasFlag('f')) {
            throw new CommandException(ChatConstant.ERROR_CYCLE_DURING_MATCH.getMessage(ChatUtil.getLocale(sender)));
        } else if (match.isStarting()) {
            Countdown.stopCountdowns(match);
        } else if (match.isRunning()) {
            if (cmd.hasFlag('n')) GameHandler.getGameHandler().getMatch().end();
            else GameHandler.getGameHandler().getMatch().end(TimeLimit.getMatchWinner());
        }
    }
}
