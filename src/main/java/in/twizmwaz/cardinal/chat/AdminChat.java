package in.twizmwaz.cardinal.chat;

import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChat implements CommandExecutor {

    public static void sendAdminMessage(String msg, Player sender){
        for (Player player : Bukkit.getOnlinePlayers()) {
            TeamModule team = TeamUtils.getTeamByPlayer(sender); //Gets the team of the online player
            if (player.isOp()) {
                player.sendMessage(ChatColor.WHITE + "[" + ChatColor.GOLD + "A" + ChatColor.WHITE + "] " + team.getColor() + sender.getDisplayName() + ChatColor.WHITE + ": " + ChatColor.WHITE + msg);
            }
        }
    }

    public static String[] commands = {"a"};

    public AdminChat() {
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        String command = cmd.getName();
        if (command.equalsIgnoreCase("a")) {
            Player player = (Player) sender;
            if (player.isOp()) {
                if (args.length == 0) {
                    //Implement this later once toggle permissions are introduced
                } else {
                    String msg = "";
                    for (int i = 0; i < args.length; i++) {
                        msg += args[i] + " ";
                    }
                    msg = msg.trim();
                    sendAdminMessage(msg, player);
                }
            }
        }
        return false;
    }
}
