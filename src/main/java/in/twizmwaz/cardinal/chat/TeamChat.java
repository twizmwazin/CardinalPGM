package in.twizmwaz.cardinal.chat;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChat implements CommandExecutor {

    //Used to send team messages from a player
    public static void sendTeamMessage(String msg, Player sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamModule playerTeam = TeamUtils.getTeamByPlayer(player); //Gets the team of the online player
            TeamModule senderTeam = TeamUtils.getTeamByPlayer(sender); //Gets the team of the sender
            if (playerTeam == senderTeam) {
                player.sendMessage(senderTeam.getColor() + "[Team] " + sender.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.WHITE + msg);
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

    public static String[] commands = {"t"};

    public TeamChat() {
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        String command = cmd.getName();
        if (command.equalsIgnoreCase("t")) {
            Player player = (Player) sender;
            if (args.length == 0) {
                //Implement this later once toggle permissions are introduced
            } else {
                String msg = "";
                for (int i = 0; i < args.length; i++) {
                    msg += args[i] + " ";
                }
                msg = msg.trim();
                sendTeamMessage(msg, player);
            }
        }
        return false;
    }
}
