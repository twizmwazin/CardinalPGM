package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.*;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.cycleTimer.CycleTimerModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time]", flags = "f")
    @CommandPermissions("cardinal.match.cycle")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if(cmd.hasFlag('f')){
                TeamModule team = TeamUtils.getTeamByName(cmd.getFlag('f'));
                GameHandler.getGameHandler().getMatch().end(team);
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
        CycleTimerModule timer = GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimerModule.class);
        timer.setOriginalState(GameHandler.getGameHandler().getMatch().getState());
        timer.setCancelled(true);
        timer.startTimer(cmd.argsLength() > 0 ? cmd.getInteger(0) : 30);
    }

    @Command(aliases = {"setnext", "sn"}, desc = "Sets the next map.", usage = "[map]", min = 1)
    @CommandPermissions("cardinal.match.setnext")
    public static void setNext(final CommandContext cmd, CommandSender sender) throws CommandException {
        String input = cmd.getJoinedStrings(0).replaceAll(" ", "");
        LoadedMap nextMap = null;
        for (LoadedMap loadedMap : GameHandler.getGameHandler().getRotation().getLoaded()) {
            if (loadedMap.getName().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(input.toLowerCase())) {
                nextMap = loadedMap;
            }
        }
        if (nextMap == null) {
            for (LoadedMap loadedMap : GameHandler.getGameHandler().getRotation().getLoaded()) {
                if (loadedMap.getName().toLowerCase().replaceAll(" ", "").startsWith(input.toLowerCase())) {
                    nextMap = loadedMap;
                }
            }
        }
        if (nextMap == null) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_MAP_MATCH).getMessage(ChatUtils.getLocale(sender)));
        } else {
            GameHandler.getGameHandler().getCycle().setMap(nextMap);
            sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_SET, ChatColor.GOLD + nextMap.getName() + ChatColor.DARK_PURPLE).getMessage(ChatUtils.getLocale(sender)));
        }
    }
    
    @Command(aliases = {"recycle", "rc"}, desc = "Cycles to the current map.", usage = "[time]", flags = "f")
    @CommandPermissions("cardinal.match.cycle")
    public static void recycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if(cmd.hasFlag('f')){
                TeamModule team = TeamUtils.getTeamByName(cmd.getFlag('f'));
                GameHandler.getGameHandler().getMatch().end(team);
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(ChatUtils.getLocale(sender)));
        GameHandler.getGameHandler().getCycle().setMap(GameHandler.getGameHandler().getMatch().getLoadedMap());
        CycleTimerModule timer = GameHandler.getGameHandler().getMatch().getModules().getModule(CycleTimerModule.class);
        timer.setOriginalState(GameHandler.getGameHandler().getMatch().getState());
        timer.setCancelled(true);
        timer.startTimer(cmd.argsLength() > 0 ? cmd.getInteger(0) : 30);
    }
}
