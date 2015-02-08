package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.event.ScoreboardUpdateEvent;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamCommand {

    @Command(aliases = {"team"}, desc = "Manage the teams in the match", usage = "<force, alias, shuffle> [player, old team] [force team, new team]", min = 1)
    public static void team(final CommandContext cmd, CommandSender sender) {
        if (cmd.getString(0).equalsIgnoreCase("force")) {
            if (cmd.argsLength() >= 3) {
                if (Bukkit.getPlayer(cmd.getString(1)) != null) {
                    String msg = "";
                    for (int i = 2; i < cmd.argsLength(); i++) {
                        msg += cmd.getString(i) + " ";
                    }
                    msg = msg.trim();
                    if (TeamUtils.getTeamByName(msg) != null) {
                        TeamModule team = TeamUtils.getTeamByName(msg);
                        team.add(Bukkit.getPlayer(cmd.getString(1)), true, ChatColor.GRAY + "You were forced to " + team.getCompleteName());
                        sender.sendMessage(team.getColor() + Bukkit.getPlayer(cmd.getString(1)).getDisplayName() + ChatColor.GRAY + " forced to " + team.getCompleteName());
                    } else { sender.sendMessage(ChatColor.RED + "Invalid team specified!"); }
                } else { sender.sendMessage(ChatColor.RED + "Player specified isn't online!"); }
            } else { sender.sendMessage(ChatColor.RED + "Too few arguments. \n" + "/team <force> <player> <force team>"); }

        } else if (cmd.getString(0).equalsIgnoreCase("alias")) {
            if (cmd.argsLength() >= 3) {
                if (TeamUtils.getTeamByName(cmd.getString(1)) != null) {
                    if (TeamUtils.getTeamByName(cmd.getString(1)).isObserver()) { sender.sendMessage(ChatColor.RED + "You can't rename observers!"); }
                    String msg = "";
                    for (int i = 2; i < cmd.argsLength(); i++) {
                        msg += cmd.getString(i) + " ";
                    }
                    msg = msg.trim();
                    TeamModule team = TeamUtils.getTeamByName(cmd.getString(1));
                    Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.GRAY + " renamed to " + team.getColor() + msg);
                    team.setName(msg);
                    Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent());
                } else { sender.sendMessage(ChatColor.RED + "Invalid team specified!"); }
            } else { sender.sendMessage(ChatColor.RED + "Too few arguments! \n" + "/team <alias> <old team> <new team>"); }

        } else if (cmd.getString(0).equalsIgnoreCase("shuffle")) {
            List<Player> playersToShuffle = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (TeamUtils.getTeamByPlayer(player) != null) {
                    if (!TeamUtils.getTeamByPlayer(player).isObserver()) {
                        playersToShuffle.add(player);
                        TeamModule observers = TeamUtils.getTeamById("observers");
                        observers.add(player, true, "");
                    }
                }
            }
            while (playersToShuffle.size() > 0) {
                Player player = playersToShuffle.get(new Random().nextInt(playersToShuffle.size()));
                TeamModule team = TeamUtils.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
                team.add(player, true);
                playersToShuffle.remove(player);
            }
            Bukkit.broadcastMessage(ChatColor.GREEN + "Teams have been shuffled!");
        } else { sender.sendMessage(ChatColor.RED + "Invalid arguments. \n" + "/team <force, alias, shuffle> [player, old team] [force team, new team]"); }
    }
}
