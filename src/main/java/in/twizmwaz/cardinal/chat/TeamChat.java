package in.twizmwaz.cardinal.chat;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChat {

    //Used to send team messages from a player
    public static void sendTeamMessage(String msg, Player sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamModule playerTeam = TeamUtils.getTeamByPlayer(player); //Gets the team of the online player
            TeamModule senderTeam = TeamUtils.getTeamByPlayer(sender); //Gets the team of the sender
            if (playerTeam == senderTeam) {
                player.sendMessage(senderTeam.getColor() + "[Team] " + sender.getDisplayName() + ": " + ChatColor.WHITE + msg);
            }
        }
    }

    //Used for the plugin to send messages to a team
    public static void sendToTeam(String msg, TeamModule team) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamModule playerTeam = TeamUtils.getTeamByPlayer(player);
            if (team == playerTeam) {
                player.sendMessage(msg);
            }
        }
    }

    @Command(aliases = {"team", "t"}, desc = "Send a message to all players on your team")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        if (cmd.argsLength() == 0) {
            //Implement this later once toggle permissions are introduced
        } else {
            String msg = "";
            for (int i = 0; cmd.argsLength() > i; i++) {
                msg += cmd.getString(i);
            }
            sendTeamMessage(msg, player);
        }
    }
}
