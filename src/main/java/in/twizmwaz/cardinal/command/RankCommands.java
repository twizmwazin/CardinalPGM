package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommands {

    @Command(aliases = {"add"}, desc = "Give a player a rank.", min = 2, usage = "<player> <rank>")
    @CommandPermissions("ranks.add")
    public static void add(final CommandContext args, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(args.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        Rank rank = Rank.getRank(args.getString(1));
        if (rank == null) {
            throw new CommandException(ChatConstant.ERROR_NO_RANK_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (rank.contains(player.getUniqueId())) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_HAS_RANK, player.getName(), rank.getName()).getMessage(ChatUtil.getLocale(sender)));
        }
        rank.add(player.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_RANK_GIVEN, Players.getName(player) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(sender)));
        player.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OWN_RANK_GIVEN, Players.getName(sender) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(player)));
    }

    @Command(aliases = {"remove"}, desc = "Remove a player's rank.", min = 2, usage = "<player> <rank>")
    @CommandPermissions("ranks.remove")
    public static void remove(final CommandContext args, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(args.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        Rank rank = Rank.getRank(args.getString(1));
        if (rank == null) {
            throw new CommandException(ChatConstant.ERROR_NO_RANK_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!rank.contains(player.getUniqueId())) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_DOESNT_HAVE_RANK, player.getName(), rank.getName()).getMessage(ChatUtil.getLocale(sender)));
        }
        rank.remove(player.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_RANK_REMOVED, Players.getName(player) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(sender)));
        player.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OWN_RANK_REMOVED, Players.getName(sender) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(player)));
    }

    @Command(aliases = {"list"}, desc = "List the ranks.")
    public static void list(final CommandContext args, CommandSender sender) {
        if (Rank.getRanks().size() == 0) {
            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_NO_RANKS).getMessage(ChatUtil.getLocale(sender)));
        } else {
            sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_RANKS).getMessage(ChatUtil.getLocale(sender)));
            for (Rank rank : Rank.getRanks()) {
                sender.sendMessage(" " + (rank.getFlair().equals("") ? "" : rank.getFlair() + " ") + ChatColor.GRAY + rank.getName());
            }
            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_RANKS_MORE_INFO).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    public static class RankParentCommand {
        @Command(aliases = {"rank", "ranks"}, desc = "Manage ranks.", min = 1)
        @NestedCommand({RankCommands.class})
        public static void rank(final CommandContext args, CommandSender sender) throws CommandException {
        }
    }

}
