package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.chatChannels.ChatChannel;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;

import java.util.concurrent.Callable;

public class ChatCommands {

    @Command(aliases = {"g", "global", "shout"}, desc = "Talk in global chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.global")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtil.getLocale(sender);
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtil.ChannelType.GLOBAL)));
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_DEFAULT_CHANNEL_GLOBAL).getMessage(locale));
            }
            if (cmd.argsLength() > 0) {
                if (GameHandler.getGameHandler().getGlobalMute() && !sender.hasPermission("cardinal.globalmute.override")) {
                    throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtil.getLocale(sender)));
                }
                String message = assembleMessage(cmd);
                if (message.trim().equals("")) return;
                ChatUtil.getGlobalChannel().sendMessage("<" + Teams.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ">: " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"a", "admin"}, desc = "Talk in admin chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.admin")
    public static void admin(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtil.getLocale(sender);
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtil.ChannelType.ADMIN)));
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_DEFAULT_CHANNEL_ADMIN).getMessage(locale));
            }
            if (cmd.argsLength() > 0) {
                String message = assembleMessage(cmd);
                if (message.trim().equals("")) return;
                ChatUtil.getAdminChannel().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + Teams.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message);
                Bukkit.getConsoleSender().sendMessage("[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + Teams.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message);
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    @Command(aliases = {"t"}, desc = "Talk in team chat.", usage = "<message>")
    @CommandPermissions("cardinal.chat.team")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        String locale = ChatUtil.getLocale(sender);
        if (sender instanceof Player) {
            if (cmd.argsLength() == 0) {
                ((Player) sender).setMetadata("default-channel", new LazyMetadataValue(GameHandler.getGameHandler().getPlugin(), LazyMetadataValue.CacheStrategy.NEVER_CACHE, new Channel(ChatUtil.ChannelType.TEAM)));
                sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.UI_DEFAULT_CHANNEL_TEAM).getMessage(locale));
            }
            if (cmd.argsLength() > 0) {
                if (GameHandler.getGameHandler().getGlobalMute() && !sender.hasPermission("cardinal.globalmute.override"))
                    throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtil.getLocale(sender)));
                Optional<TeamModule> team = Teams.getTeamByPlayer((Player) sender);
                if (team.isPresent()) {
                    ChatChannel channel = Teams.getTeamChannel(team);
                    String message = assembleMessage(cmd);
                    if (message.trim().equals("")) return;
                    channel.sendLocalizedMessage(new UnlocalizedChatMessage(Teams.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message));
                    Bukkit.getConsoleSender().sendMessage(team.get().getColor() + "[" + team.get().getName() + "] " + ((Player) sender).getDisplayName() + ChatColor.RESET + ": " + message);
                } else {
                    if (GameHandler.getGameHandler().getGlobalMute() && !sender.hasPermission("cardinal.globalmute.override"))
                        throw new CommandException(ChatConstant.ERROR_GLOBAL_MUTE_ENABLED.asMessage().getMessage(ChatUtil.getLocale(sender)));
                    String message = assembleMessage(cmd);
                    if (message.trim().equals("")) return;
                    ChatUtil.getGlobalChannel().sendMessage("<" + Teams.getTeamColorByPlayer((Player) sender) + ((Player) sender).getDisplayName() + ChatColor.RESET + ">: " + message);
                }
            }
        } else throw new CommandException("Console cannot use this command.");
    }

    private static String assembleMessage(CommandContext context) {
        return context.getJoinedStrings(0);
    }

    public static class Channel implements Callable {

        private final ChatUtil.ChannelType channel;

        protected Channel(final ChatUtil.ChannelType channel) {
            this.channel = channel;
        }

        @Override
        public Object call() throws Exception {
            return channel;
        }
    }

}
