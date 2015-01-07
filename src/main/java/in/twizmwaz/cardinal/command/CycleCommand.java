package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import org.bukkit.command.CommandSender;

/**
 * Created by kevin on 10/31/14.
 */
public class CycleCommand {

    @Command(aliases = {"cycle"}, desc = "Cycles the world and loads a new world.", usage = "[time]")
    @CommandPermissions("cardinal.match.cycle")
    public static void cycle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isRunning()) {
            throw new CommandException("Cannot cycle while the match is running!");
        }
        try {
            GameHandler.getGameHandler().startCycleTimer(cmd.getInteger(0));
        } catch (IndexOutOfBoundsException e) {
            GameHandler.getGameHandler().startCycleTimer(30);
        }


    }

    @Command(aliases = {"setnext", "sn"}, desc = "Sets the next map.", usage = "[map]", min = 0)
    @CommandPermissions("cardinal.match.setnext")
    public static void setNext(final CommandContext cmd, CommandSender sender) throws CommandException {
        String input = "";
        try {
            for (int i = 0; i < cmd.argsLength(); i++) {
                input = input + cmd.getString(i);
            }
        } catch (IndexOutOfBoundsException ex) {
            throw new CommandException("Please specify a map!");
        }
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
            throw new CommandException("No map named " + input);
        } else {
            GameHandler.getGameHandler().getCycle().setMap(nextMap);
            sender.sendMessage(ChatColor.DARK_PURPLE + "Next map set to " + ChatColor.GOLD + nextMap.getName());
        }
    }

}
