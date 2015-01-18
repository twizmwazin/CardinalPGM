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

public class TeamChat {

    //used to send team messages from a player
    public static void sendTeamMessage(String msg, Player p){
        for(Player player : Bukkit.getOnlinePlayers()){
            TeamModule ReceiverTeam = TeamUtils.getTeamByPlayer(player); //Gets the team of the online player
            TeamModule SenderTeam = TeamUtils.getTeamByPlayer(p); //Gets the team of the sender
            if (ReceiverTeam.getName() == SenderTeam.getName()) {
                player.sendMessage(SenderTeam.getColor() + "[Team] " + p.getDisplayName() + ": " + ChatColor.WHITE + msg);
            }
        }
    }

    //used for the plugin to send messages to a team
    public static void SendToTeam(String msg, TeamModule SenderTeam){
        for(Player p : Bukkit.getOnlinePlayers()){
            TeamModule ReceiverTeam = TeamUtils.getTeamByPlayer(p);
            if (SenderTeam.getName() == ReceiverTeam.getName()) {
                p.sendMessage(msg);
            }
        }
    }

    @Command(aliases = {"team","t"}, desc = "Send a message to all player on your team")
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = (Player) sender;
        if(cmd.argsLength() == 0){
            Chat.setChannelTeam();
            player.sendMessage(org.bukkit.ChatColor.GOLD + "Default channel changed to team");
        } else {
            String msg = "";
            for (int i = 0; cmd.argsLength() > i; i++) {
                msg += cmd.getString(i);
            }
            sendTeamMessage(msg, player);
        }
    }


}
