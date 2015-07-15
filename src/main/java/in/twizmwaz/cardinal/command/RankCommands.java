package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.chat.ChatConstant;
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
            throw new CommandException(player.getName() + " already has the " + rank.getName() + " rank.");
        }
        rank.add(player.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + "You have given " + Players.getName(player) + ChatColor.GRAY + " the " + rank.getName() + " rank.");
        player.sendMessage(Players.getName(sender) + ChatColor.GRAY + " has given you the " + rank.getName() + " rank.");
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
            throw new CommandException(player.getName() + " already doesn't have the " + rank.getName() + " rank.");
        }
        rank.remove(player.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + "You have removed " + Players.getName(player) + ChatColor.GRAY + "'s " + rank.getName() + " rank.");
        player.sendMessage(Players.getName(sender) + ChatColor.GRAY + " has removed your " + rank.getName() + " rank.");
    }

    public static class RankParentCommand {
        @Command(aliases = {"rank", "ranks"}, desc = "Manage ranks.", min = 1)
        @NestedCommand({RankCommands.class})
        public static void rank(final CommandContext args, CommandSender sender) throws CommandException {
        }
    }

}
