package in.twizmwaz.cardinal.chat;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlobalChat {

    public static void sendGlobalMessage(String msg, Player player) {
        TeamModule team = TeamUtils.getTeamByPlayer(player);
        Bukkit.broadcastMessage("<" + team.getColor() + player.getDisplayName() + ChatColor.WHITE + ">: " + msg);
    }

    @Command(aliases = {"global", "g"}, desc = "Send a message to all players on the servers")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        if (cmd.argsLength() == 0) {
            //Implement this later once toggle permissions are introduced
        } else {
            String msg = "";
            for (int i = 0; cmd.argsLength() > i; i++) {
                msg += cmd.getString(i) + " ";
            }
            msg = msg.trim();
            sendGlobalMessage(msg, player);
        }
    }
}
