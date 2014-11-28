package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 10/31/14.
 */
public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time]")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            throw new CommandException("Cannot cycle while the match is running!");
        }
        try {
            GameHandler.getGameHandler().startCycleTimer(cmd.getInteger(0));
        } catch (IndexOutOfBoundsException ex) {
            GameHandler.getGameHandler().startCycleTimer(30);
        }


    }

    @Command(aliases = {"setnext", "sn"}, desc = "Sets the next map.", usage = "[map]", min = 0)
    public static void setNext(final CommandContext cmd, CommandSender sender) throws CommandException {
        String input;
        try {
            input = cmd.getString(0);
        } catch (IndexOutOfBoundsException ex) {
            throw new CommandException("Please specify a map!");
        }
        String nextMap = null;
        for (String loadedMap : GameHandler.getGameHandler().getRotation().getLoaded()) {
            if (loadedMap.toLowerCase().startsWith(input.toLowerCase())) {
                nextMap = loadedMap;
            }
        }
        try {
            GameHandler.getGameHandler().getCycle().setMap(nextMap);
            sender.sendMessage(ChatColor.DARK_PURPLE + "Set the next map to " + ChatColor.GOLD + nextMap);
        } catch (NullPointerException ex) {
            throw new CommandException("No map named " + input);
        }
    }

}
