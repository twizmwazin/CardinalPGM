package in.twizmwaz.cardinal.chat;

import com.sk89q.minecraft.util.commands.ChatColor;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalChat implements CommandExecutor {

    public static void sendGlobalMessage(String msg, Player player) {
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        Bukkit.broadcastMessage("<" + team.getColor() + player.getDisplayName() + ChatColor.WHITE + ">: " + msg);
    }

    public static String[] commands = {"g"};

    public GlobalChat() {
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
        String command = cmd.getName();
        if (command.equalsIgnoreCase("g")) {
            Player player = (Player) sender;
            if (args.length == 0) {
                //Implement this later once toggle permissions are introduced
            } else {
                String msg = "";
                for (int i = 0; i < args.length; i++) {
                    msg += args[i] + " ";
                }
                msg = msg.trim();
                sendGlobalMessage(msg, player);
            }
        }
        return false;
    }
}
