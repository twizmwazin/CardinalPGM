package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannelModule;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommands {

    @Command(aliases = {"g"}, desc = "Talk in global chat.")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatChannelModule channel = GameHandler.getGameHandler().getMatch().getModules().getModule(GlobalChannel.class);
        if (sender instanceof Player) {
            TeamModule team = TeamUtils.getTeamByPlayer((Player) sender);
            String message = assembleMessage(cmd);
            channel.sendMessage("<" + team.getColor() + ((Player) sender).getDisplayName() + ChatColor.RESET + ">:" + message);
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"a"}, desc = "Talk in admin chat.")
    public static void admin(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatChannelModule channel = GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
        if (sender instanceof Player) {
            TeamModule team = TeamUtils.getTeamByPlayer((Player) sender);
            String message = assembleMessage(cmd);
            channel.sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + team.getColor() + ((Player) sender).getDisplayName() + ChatColor.RESET + ":"  + message);
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"t"}, desc = "Talk in team chat.")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            TeamModule team = TeamUtils.getTeamByPlayer((Player) sender);
            ChatChannelModule channel = TeamUtils.getTeamChannel(team);
            String message = assembleMessage(cmd);
            channel.sendMessage(team.getColor() + "[Team] " + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message);
        } else throw new CommandException("Console cannot use this command.");
    }
    
    private static String assembleMessage(CommandContext context) {
        String results = "";
        String[] args = {""};
        try {
            args = (String[]) FieldUtils.readDeclaredField(context, "originalArgs", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < args.length; i++) {
            results += args[i] + " ";
        }
        return results.trim();
    }
    
}
