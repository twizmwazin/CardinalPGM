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

    public static void sendGlobalMessage(String msg, Player p) {
        TeamModule team = TeamUtils.getTeamByPlayer(p);
        Bukkit.broadcastMessage("<" + team.getColor() + p.getDisplayName() + ChatColor.WHITE + "> " + ChatColor.WHITE + msg);
    }

    @Command(aliases = {"global","g"}, desc = "Send a message to all player on the servers")
    public static void global(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        if(cmd.argsLength() == 0){
            Chat.setChannelGloabl();
            player.sendMessage(org.bukkit.ChatColor.GOLD + "Default channel changed to global");
        } else {
            String msg = "";
            for (int i = 0; cmd.argsLength() > i; i++) {
                msg += cmd.getString(i);
            }
            sendGlobalMessage(msg, player);
        }
    }
}
