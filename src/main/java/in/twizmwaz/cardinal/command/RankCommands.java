package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.rank.Rank;
import in.twizmwaz.cardinal.util.AsyncCommand;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankCommands {

    @Command(aliases = {"add", "give"}, desc = "Give a player a rank.", min = 2, usage = "<player> <rank>")
    public static void add(final CommandContext args, CommandSender sender) throws CommandException {
        final Rank rank = Rank.getRank(args.getString(1));
        if (rank == null) {
            throw new CommandException(ChatConstant.ERROR_NO_RANK_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!sender.hasPermission("cardinal.ranks.add") && !sender.hasPermission("cardinal.ranks.add.*") && !sender.hasPermission("cardinal.ranks.add." + rank.getName().toLowerCase())) {
            throw new CommandException(ChatConstant.ERROR_NO_PERMISSION.getMessage(ChatUtil.getLocale(sender)));
        }
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new AsyncCommand(args, sender) {
            @Override
            public void run() {
                OfflinePlayer player = args.getString(0).startsWith("@") ? Bukkit.getOfflinePlayer(args.getString(0).substring(1)) : Bukkit.getPlayer(args.getString(0));
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
                    return;
                }
                if (rank.contains(player.getUniqueId())) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_HAS_RANK, player.getName(), rank.getName()).getMessage(ChatUtil.getLocale(sender)));
                    return;
                }
                rank.add(player.getUniqueId());
                if (sender.equals(player)) {
                    sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_SELF_RANK_GIVEN, rank.getName()).getMessage(ChatUtil.getLocale(sender)));
                } else {
                    sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_RANK_GIVEN, Players.getName(player) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(sender)));
                    if (player.isOnline())
                        player.getPlayer().sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OWN_RANK_GIVEN, Players.getName(sender) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(player.getPlayer())));
                }
            }
        });
    }

    @Command(aliases = {"remove"}, desc = "Remove a player's rank.", min = 2, usage = "<player> <rank>")
    public static void remove(final CommandContext args, CommandSender sender) throws CommandException {
        final Rank rank = Rank.getRank(args.getString(1));
        if (rank == null) {
            throw new CommandException(ChatConstant.ERROR_NO_RANK_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (!sender.hasPermission("cardinal.ranks.remove") && !sender.hasPermission("cardinal.ranks.remove.*") && !sender.hasPermission("cardinal.ranks.remove." + rank.getName().toLowerCase())) {
            throw new CommandException(ChatConstant.ERROR_NO_PERMISSION.getMessage(ChatUtil.getLocale(sender)));
        }
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new AsyncCommand(args, sender) {
            @Override
            public void run() {
                OfflinePlayer player = args.getString(0).startsWith("@") ? Bukkit.getOfflinePlayer(args.getString(0).substring(1)) : Bukkit.getPlayer(args.getString(0));
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
                    return;
                }
                if (!rank.contains(player.getUniqueId())) {
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_DOESNT_HAVE_RANK, player.getName(), rank.getName()).getMessage(ChatUtil.getLocale(sender)));
                    return;
                }
                rank.remove(player.getUniqueId());
                if (sender.equals(player)) {
                    sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_SELF_RANK_REMOVED, rank.getName()).getMessage(ChatUtil.getLocale(sender)));
                } else {
                    sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_RANK_REMOVED, Players.getName(player) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(sender)));
                    if (player.isOnline())
                        player.getPlayer().sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_OWN_RANK_REMOVED, Players.getName(sender) + ChatColor.GRAY, rank.getName()).getMessage(ChatUtil.getLocale(player.getPlayer())));
                }
            }
        });
    }

    @Command(aliases = {"list"}, desc = "List the ranks.")
    public static void list(final CommandContext args, CommandSender sender) throws CommandException {
        if (Rank.getRanks().size() == 0) {
            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_NO_RANKS).getMessage(ChatUtil.getLocale(sender)));
        } else if (args.argsLength() == 0) {
            sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_RANKS).getMessage(ChatUtil.getLocale(sender)));
            for (Rank rank : Rank.getRanks()) {
                sender.sendMessage(" " + (rank.getFlair().equals("") ? "" : rank.getFlair() + " ") + ChatColor.GRAY + rank.getName());
            }
            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_RANKS_MORE_INFO).getMessage(ChatUtil.getLocale(sender)));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new AsyncCommand(args, sender) {
                @Override
                public void run() {
                    OfflinePlayer player = args.getString(0).startsWith("@") ? Bukkit.getOfflinePlayer(args.getString(0).substring(1)) : Bukkit.getPlayer(args.getString(0));
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
                        return;
                    }
                    sender.sendMessage(ChatColor.GOLD + new LocalizedChatMessage(ChatConstant.GENERIC_RANKS).getMessage(ChatUtil.getLocale(sender)));
                    List<Rank> playerRanks = new ArrayList<>();
                    for (Rank rank : Rank.getRanks()) {
                        if (rank.contains(player.getUniqueId())) playerRanks.add(rank);
                    }
                    StringBuilder resultPlayerRanks = new StringBuilder().append(ChatColor.GRAY + " " + Players.getName(player) + ChatColor.GRAY + " - ");
                    for (int i = 0; i < playerRanks.size(); i++) {
                        resultPlayerRanks.append(playerRanks.get(i).getFlair() + " " + ChatColor.GRAY).append(playerRanks.get(i).getName()).toString();
                        if (i < playerRanks.size() - 1) resultPlayerRanks.append(", ");
                    }
                    sender.sendMessage(resultPlayerRanks.toString());
                }
            });
        }
    }

    @Command(aliases = {"info"}, desc = "View info about a certain rank.", min = 1, usage = "[rank]", flags = "a")
    public static void info(final CommandContext args, CommandSender sender) throws CommandException {
        Rank rank = Rank.getRank(args.getString(0));
        if (rank == null) {
            throw new CommandException(ChatConstant.ERROR_NO_RANK_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        sender.sendMessage(ChatColor.GOLD + ChatConstant.GENERIC_RANK_INFO.asMessage().getMessage(ChatUtil.getLocale(sender)) + ": ");
        sender.sendMessage(ChatColor.GRAY + " " + ChatConstant.MISC_NAME.asMessage().getMessage(ChatUtil.getLocale(sender)) + ": " + (rank.getFlair().equals("") ? "" : rank.getFlair() + " ") + ChatColor.GRAY + rank.getName());
        sender.sendMessage(ChatColor.GRAY + " " + ChatConstant.MISC_STAFF.asMessage().getMessage(ChatUtil.getLocale(sender)) + ": " + rank.isStaffRank());
        if (rank.isDefaultRank())
            sender.sendMessage(ChatColor.GRAY + " " + ChatConstant.MISC_DEFAULT.asMessage().getMessage(ChatUtil.getLocale(sender)) + ": " + rank.isDefaultRank());
        if (rank.getParent() != null)
            sender.sendMessage(ChatColor.GRAY + " " + ChatConstant.MISC_PARENT.asMessage().getMessage(ChatUtil.getLocale(sender)) + ": " + rank.getParent());
        StringBuilder resultMembers = new StringBuilder().append(ChatColor.GRAY + " " + ChatConstant.UI_ONLINE.asMessage().getMessage(ChatUtil.getLocale(sender)) + " ");
        List<Player> rankMembers = new ArrayList<>();
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (rank.contains(player.getUniqueId())) rankMembers.add(player.getPlayer());
        }
        for (int i = 0; i < rankMembers.size(); i++) {
            resultMembers.append(Players.getName(rankMembers.get(i))).append(ChatColor.RESET);
            if (i < rankMembers.size() - 1) resultMembers.append(", ");
        }
        if (rankMembers.size() > 0) sender.sendMessage(resultMembers.toString());
        if (args.hasFlag('a')) {
            List<String> permissionList = new ArrayList<>();
            for (String permission : rank.getPermissions()) {
                permissionList.add(permission);
            }
            if (permissionList.size() > 0) {
                sender.sendMessage(ChatColor.GRAY + " " + ChatConstant.MISC_PERMISSIONS.asMessage().getMessage(ChatUtil.getLocale(sender)) + ":");
                for (int i = 0; i < permissionList.size(); i++) {
                    sender.sendMessage(ChatColor.GRAY + "  - " + permissionList.get(i));
                }
            }
        }
    }

    public static class RankParentCommand {
        @Command(aliases = {"rank", "ranks"}, desc = "Manage ranks.", min = 1)
        @NestedCommand({RankCommands.class})
        public static void rank(final CommandContext args, CommandSender sender) throws CommandException {
        }
    }

}
