package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class StartAndEndCommand {
    private static int timer;
    private static boolean waiting = false;

    @Command(aliases = {"start"}, desc = "Starts the match.", usage = "[time]", min = 1, max = 1)
    public static void start(CommandContext cmd, CommandSender sender) {
        if(!sender.hasPermission("cardinal.match.start")) { sender.sendMessage("You don't have permission."); }
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING)) {
            int time = 30;
            try {
                time = cmd.getInteger(0);
            } catch (IndexOutOfBoundsException ex) {}
            GameHandler.getGameHandler().getMatch().start(time);
        } else if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            try {
                GameHandler.getGameHandler().getMatch().getStartTimer().setTime(cmd.getInteger(0));
            } catch (NullPointerException e) {}
        } else { sender.sendMessage(ChatColor.RED + "Can't start a match right now!"); }

    }

    @Command(aliases = {"end", "finish"}, desc = "Ends the match.", usage = "[time]", min = 0, max = 0)
    public static void end(CommandContext cmd, CommandSender sender) {
        if(!sender.hasPermission("cardinal.match.end")) { sender.sendMessage("You don't have permission."); }
        if (GameHandler.getGameHandler().getMatch().getState() == MatchState.PLAYING) {
            try {
                TeamModule team = TeamUtils.getTeamByName(cmd.getString(0));
                GameHandler.getGameHandler().getMatch().end(team);
            } catch (IndexOutOfBoundsException ex) { GameHandler.getGameHandler().getMatch().end(null); }
        } else { sender.sendMessage(ChatColor.RED + "Cannot end a game that is not currently playing!"); }
    }
}
