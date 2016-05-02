package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Strings;
import in.twizmwaz.cardinal.util.polls.Polls;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PollCommands {

    @Command(aliases = {"poll"}, flags = "at:", desc = "Creates a poll to run a command", usage = "<command>", min = 1, anyFlags = true)
    @CommandPermissions("cardinal.poll.add")
    public static void poll(final CommandContext cmd, CommandSender sender) throws CommandException {
        Polls.addPoll(sender, cmd.getJoinedStrings(0), Strings.timeStringToSeconds(cmd.getFlag('t', "60")), cmd.hasFlag('a'));
    }

    @Command(aliases = {"vote"}, desc = "Votes in a poll", usage = "[id] <yes|no>", min = 1, max = 2)
    public static void vote(final CommandContext cmd, CommandSender sender) throws CommandException {
        Integer pollId = findPoll(cmd.argsLength() == 1 ? null : cmd.getInteger(0), sender);
        if (!Polls.isPoll(pollId)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_POLL_NO_SUCH_POLL, "" + pollId).getMessage(ChatUtil.getLocale(sender)));
        }
        if (!sender.hasPermission("cardinal.poll.vote") && !Polls.isAny(pollId)) {
            throw new CommandException(ChatConstant.ERROR_NO_PERMISSION.getMessage(ChatUtil.getLocale(sender)));
        }
        String voteArg = cmd.getString(cmd.argsLength() -1).toLowerCase();
        if ("yes".startsWith(voteArg) || "no".startsWith(voteArg)) {
            boolean vote = "yes".startsWith(voteArg);
            boolean success = Polls.vote(pollId, (Player) sender, vote);
            if (success) {
                sender.sendMessage((vote ? ChatColor.GREEN : ChatColor.RED) + new LocalizedChatMessage(vote ? ChatConstant.GENERIC_POLL_VOTED : ChatConstant.GENERIC_POLL_VOTED_AGAINST, ChatColor.GOLD + "" + pollId + (vote ? ChatColor.GREEN : ChatColor.RED)).getMessage(ChatUtil.getLocale(sender)));
            } else {
                throw new CommandException(ChatConstant.ERROR_POLL_ALREADY_VOTED.getMessage(ChatUtil.getLocale(sender)));
            }
        } else {
            throw new CommandException(ChatConstant.ERROR_INVALID_ARGUMENTS.getMessage(ChatUtil.getLocale(sender)) + " yes | no");
        }
    }

    @Command(aliases = {"veto"}, desc = "Vetoes a poll", min = 0, max = 1)
    @CommandPermissions("cardinal.poll.veto")
    public static void veto(final CommandContext cmd, CommandSender sender) throws CommandException {
        int pollId = findPoll(cmd.argsLength() == 0 ? null : cmd.getInteger(0), sender);
        Polls.stopPoll(pollId, sender);
    }

    private static int findPoll(Integer id, CommandSender sender) throws CommandException {
        if (id != null) {
            if (Polls.isPoll(id)) {
                return id;
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_POLL_NO_SUCH_POLL, "" + id).getMessage(ChatUtil.getLocale(sender)));
            }
        } else {
            id = Polls.getPoll();
            if (id == -1) {
                throw new CommandException(ChatConstant.ERROR_POLL_NEED_ID.getMessage(ChatUtil.getLocale(sender)));
            } else if (id == 0) {
                throw new CommandException(ChatConstant.ERROR_POLL_NO_POLLS.getMessage(ChatUtil.getLocale(sender)));
            }
            return id;
        }
    }

}
