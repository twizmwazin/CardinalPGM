package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ClassChangeEvent;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommands {

    @Command(aliases = {"classes", "classlist"}, desc = "Lists the classes available in a map.", max = 1)
    public static void classes(final CommandContext cmd, CommandSender sender) {
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null) {
            sender.sendMessage(ChatColor.RED + "------------------------ " + ChatColor.GOLD + "Classes" + ChatColor.RED + " ------------------------");
            int count = 1;
            for (ClassModule classModule : GameHandler.getGameHandler().getMatch().getModules().getModules(ClassModule.class)) {
                sender.sendMessage(count + ". " + (sender instanceof Player && ClassModule.getClassByPlayer((Player) sender) != null && ClassModule.getClassByPlayer((Player) sender).equals(classModule) ? ChatColor.GOLD + "" + ChatColor.UNDERLINE : ChatColor.GREEN + "") + classModule.getName() + ChatColor.DARK_PURPLE + " - " + ChatColor.RESET + classModule.getDescription());
            }
        } else { sender.sendMessage(ChatColor.RED + "Classes are not enabled on this map."); }
    }

    @Command(aliases = {"class"}, desc = "Allows you to change your class.", min = 0, max = 1)
    public static void classCommand(final CommandContext cmd, CommandSender sender) {
        if (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null) {
            if (cmd.argsLength() != 1) {
                if (sender instanceof Player && ClassModule.getClassByPlayer((Player) sender) != null) {
                    sender.sendMessage(ChatColor.GREEN + "Current class: " + ChatColor.GOLD + "" + ChatColor.UNDERLINE + ClassModule.getClassByPlayer((Player) sender).getName());
                    sender.sendMessage(ChatColor.DARK_PURPLE + "List all classes by typing " + ChatColor.GOLD + "'/classes'");
                }
            } else {
                String input = cmd.getJoinedStrings(0);
                if (ClassModule.getClassByName(input) != null) {
                    ClassModule.playerClass.put(((Player) sender).getUniqueId(), ClassModule.getClassByName(input));
                    ClassChangeEvent changeEvent = new ClassChangeEvent((Player) sender, ClassModule.getClassByName(input));
                    Bukkit.getServer().getPluginManager().callEvent(changeEvent);
                } else { sender.sendMessage(ChatColor.RED + "No class matched query."); }
            }
        } else { sender.sendMessage(ChatColor.RED + "Classes are not enabled on this map."); }
    }
}
