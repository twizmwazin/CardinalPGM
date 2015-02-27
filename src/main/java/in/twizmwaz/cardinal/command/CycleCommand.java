package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
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
                try {
                    TeamModule team = TeamUtils.getTeamByName(cmd.getString(0));
                    GameHandler.getGameHandler().getMatch().end(team);
                } catch (IndexOutOfBoundsException ex) {
                    GameHandler.getGameHandler().getMatch().end(null);
                }
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CYCLE_DURING_MATCH).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        if (GameHandler.getGameHandler().getCycleTimer() != null)
            GameHandler.getGameHandler().getCycleTimer().setCancelled(true);
        try {
            GameHandler.getGameHandler().startCycleTimer(cmd.getInteger(0));
        } catch (IndexOutOfBoundsException e) {
            GameHandler.getGameHandler().startCycleTimer(30);
        }
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
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_MAP_MATCH).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        } else {
            GameHandler.getGameHandler().getCycle().setMap(nextMap);
            sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_SET, ChatColor.GOLD + nextMap.getName() + ChatColor.DARK_PURPLE).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
        }
    }

}
