package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtils;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

public class ChatCommands {

    public static class Channel implements Callable {

        private final ChatUtils.ChannelType channel;

        protected Channel(final ChatUtils.ChannelType channel){
            this.channel = channel;
        }

        @Override
        public Object call() throws Exception {
            return channel;
        }
    }

    @Command(aliases = {"g", "global", "shout"}, desc = "Talk in global chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.global")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtils.ChannelType.GLOBAL)));
                sender.sendMessage(ChatColor.YELLOW + "Your default channel was changed to " + ChatColor.RED + "global");
            }
            if (cmd.argsLength() > 0) {
                if (GameHandler.getGameHandler().getGlobalMute() && !PermissionModule.isStaff(((Player) sender)))
                    throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtils.getLocale(sender)));
                String message = assembleMessage(cmd);
                if (message.trim().equals("")) return;
                ChatUtils.getGlobalChannel().sendMessage("<" + TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ">: " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"a", "admin"}, desc = "Talk in admin chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.admin")
    public static void admin(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtils.ChannelType.ADMIN)));
                sender.sendMessage(ChatColor.YELLOW + "Your default channel was changed to " + ChatColor.RED + "admin");
            }
            if (cmd.argsLength() > 0) {
                String message = assembleMessage(cmd);
                if (message.trim().equals("")) return;
                ChatUtils.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message);
                Bukkit.getLogger().info("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + TeamUtils.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ":" + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"t"}, desc = "Talk in team chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.team")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtils.ChannelType.TEAM)));
                sender.sendMessage(ChatColor.YELLOW + "Your default channel was changed to " + ChatColor.RED + "team");
            }
            if (cmd.argsLength() > 0) {
                if (GameHandler.getGameHandler().getGlobalMute() && !PermissionModule.isStaff(((Player) sender)))
                    throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtils.getLocale(sender)));
                TeamModule team = TeamUtils.getTeamByPlayer((Player) sender);
                TeamChannel channel = TeamUtils.getTeamChannel(team);
                String message = assembleMessage(cmd);
                if (message.trim().equals("")) return;
                channel.sendLocalizedMessage(new UnlocalizedChatMessage(channel.getTeam().getColor() + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message));
                Bukkit.getLogger().info(team.getColor() + "[" + team.getName() + "] " + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }
    
    private static String assembleMessage(CommandContext context) {
        return context.getJoinedStrings(0);
    }
    
}
