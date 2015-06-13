package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.cycleTimer.CycleTimerModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time] [map]", flags = "f")
    @CommandPermissions("cardinal.match.cycle")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (cmd.hasFlag('f')) {
                TeamModule team = TeamUtils.getTeamByName(cmd.getFlag('f'));
                GameHandler.getGameHandler().getMatch().end(team);
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
        if (cmd.argsLength() > 1) {
            LoadedMap next = getMap(cmd.getJoinedStrings(1));
            if (next == null) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_MAP_MATCH).getMessage(ChatUtils.getLocale(sender)));
            } else {
                setCycleMap(next);
            }
        }
        CycleTimerModule timer = GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimerModule.class);
        timer.setOriginalState(GameHandler.getGameHandler().getMatch().getState());
        timer.setCancelled(true);
        timer.startTimer(cmd.argsLength() > 0 ? cmd.getInteger(0) : 30);
    }

    @Command(aliases = {"setnext", "sn"}, desc = "Sets the next map.", usage = "[map]", min = 1)
    @CommandPermissions("cardinal.match.setnext")
    public static void setNext(final CommandContext cmd, CommandSender sender) throws CommandException {
        String input = cmd.getJoinedStrings(0).replaceAll(" ", "");
        LoadedMap nextMap = getMap(cmd.getJoinedStrings(0));
        if (nextMap == null) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_MAP_MATCH).getMessage(ChatUtils.getLocale(sender)));
        } else {
            setCycleMap(nextMap);
            sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_SET, ChatColor.GOLD + nextMap.getName() + ChatColor.DARK_PURPLE).getMessage(ChatUtils.getLocale(sender)));
        }
    }

    @Command(aliases = {"recycle", "rc"}, desc = "Cycles to the current map.", usage = "[time]", flags = "f")
    @CommandPermissions("cardinal.match.cycle")
    public static void recycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if (cmd.hasFlag('f')) {
                TeamModule team = TeamUtils.getTeamByName(cmd.getFlag('f'));
                GameHandler.getGameHandler().getMatch().end(team);
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
        setCycleMap(GameHandler.getGameHandler().getMatch().getLoadedMap());
        CycleTimerModule timer = GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimerModule.class);
        timer.setOriginalState(GameHandler.getGameHandler().getMatch().getState());
        timer.setCancelled(true);
        timer.startTimer(cmd.argsLength() > 0 ? cmd.getInteger(0) : 30);
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
}
