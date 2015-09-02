package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class JoinCommand {

    @Command(aliases = {"join", "play"}, desc = "Join a team.", usage = "[team]")
    public static void join(final CommandContext cmd, CommandSender sender) throws CommandException {
        Map<String,String> playerTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getPlayersTeams();
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        if (playerTeams.containsKey(sender.getName())) {
            throw new CommandException(ChatUtil.getWarningMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.ERROR_NO_PERMISSION).getMessage(ChatUtil.getLocale(sender))));
        }
        try {
            Teams.setPlayerTeam((Player) sender, cmd.argsLength() > 0 ? cmd.getJoinedStrings(0) : "");
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Command(aliases = {"leave", "obs", "observe", "spectate"}, desc = "Leave the game.")
    public static void leave(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        try {
            Teams.setPlayerTeam((Player) sender, Teams.getTeamById("observers").get().getName());
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }

}
