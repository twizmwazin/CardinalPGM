package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand {

    @Command(aliases = {"join", "play"}, desc = "Join a team.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        try {
            Teams.setPlayerTeam((Player) sender, cmd.argsLength() > 0 ? cmd.getJoinedStrings(0) : "");
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Command(aliases = {"leave", "obs", "observe", "spectate"}, desc = "Leave the game.", usage = "[player]", max = 1)
    public static void leave(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player) && cmd.argsLength() < 1) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (cmd.argsLength() > 0 && !sender.hasPermission("cardinal.team.force")) {
            throw new CommandPermissionsException();
        }
        Player player = cmd.argsLength() < 1 ? (Player) sender : Bukkit.getPlayer(cmd.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        try {
            Teams.setPlayerTeam(player, Teams.getObserverTeam());
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}
