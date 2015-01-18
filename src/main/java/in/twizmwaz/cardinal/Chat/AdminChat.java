package in.twizmwaz.cardinal.Chat;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChat {

    public static void sendAdminessage(String msg, Player p){
        for(Player player : Bukkit.getOnlinePlayers()){
            TeamModule Team = TeamUtils.getTeamByPlayer(p); //Gets the team of the online player
            if (player.isOp()) {
                player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "A" + ChatColor.GRAY + "] " + Team.getColor() + p.getDisplayName() + ": " + ChatColor.WHITE + msg);
            }
        }
    }

    @Command(aliases = {"admin","a"}, desc = "Send a message to all ops")
    public static void admin(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        if (player.isOp()) {
            if (cmd.argsLength() == 0) {
                Chat.setChannelAdmin();
                player.sendMessage(ChatColor.GOLD + "Default channel changed to admin");
            } else {
                String msg = "";
                for (int i = 0; cmd.argsLength() > i; i++) {
                    msg += cmd.getString(i);
                }
                sendAdminessage(msg, player);
            }
        }
    }
}
