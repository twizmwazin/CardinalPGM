package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.command.CommandSender;

public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time]", flags = "f")
    public static void cycle(final CommandContext cmd, CommandSender sender) {
        if(!(sender.hasPermission("cardinal.match.cycle"))) { sender.sendMessage(ChatColor.RED + "You don't have permission."); }
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            if(cmd.hasFlag('f')){
                try {
                    TeamModule team = TeamUtils.getTeamByName(cmd.getString(0));
                    GameHandler.getGameHandler().getMatch().end(team);
                } catch (IndexOutOfBoundsException ex) {
                    GameHandler.getGameHandler().getMatch().end(null);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Cannot cycle while the match is running!");
            }
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING))
            sender.sendMessage(ChatColor.RED + "Cannot cycle while the match is running!");
        if (GameHandler.getGameHandler().getCycleTimer() != null)
            GameHandler.getGameHandler().getCycleTimer().setCancelled(true);
        try {
            GameHandler.getGameHandler().startCycleTimer(cmd.getInteger(0));
        } catch (IndexOutOfBoundsException e) { GameHandler.getGameHandler().startCycleTimer(30); }
    }

    @Command(aliases = {"setnext", "sn"}, desc = "Sets the next map.", usage = "[map]", min = 0)
    public static void setNext(final CommandContext cmd, CommandSender sender) {
        if(!(sender.hasPermission("cardinal.match.setnext"))) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        }
        String input = "";
        try {
            for (int i = 0; i < cmd.argsLength(); i++) {
                input = input + cmd.getString(i);
            }
        } catch (IndexOutOfBoundsException ex) { sender.sendMessage(ChatColor.RED + "Please specify a map!"); }
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
            sender.sendMessage(ChatColor.RED + "No map named " + input);
        } else {
            GameHandler.getGameHandler().getCycle().setMap(nextMap);
            sender.sendMessage(ChatColor.DARK_PURPLE + "Next map set to " + ChatColor.GOLD + nextMap.getName());
        }
    }

}
