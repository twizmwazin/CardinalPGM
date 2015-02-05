package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
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
    public static void team(final CommandContext cmd, CommandSender sender) throws CommandException {
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
                        if (!team.contains(sender)) {
                            team.add(Bukkit.getPlayer(cmd.getString(1)), true, ChatColor.GRAY + "You were forced to " + team.getCompleteName());
                            sender.sendMessage(team.getColor() + Bukkit.getPlayer(cmd.getString(1)).getDisplayName() + ChatColor.GRAY + " forced to " + team.getCompleteName());
                        } else {
                            throw new CommandException("Player is already on " + team.getCompleteName());
                        }
                    } else {
                        throw new CommandException("Invalid team specified!");
                    }
                } else {
                    throw new CommandException("Player specified is not online!");
                }
            } else {
                throw new CommandUsageException("Too few arguments.", "/team <force> <player> <force team>");
            }
        } else if (cmd.getString(0).equalsIgnoreCase("alias")) {
            if (cmd.argsLength() >= 3) {
                if (TeamUtils.getTeamByName(cmd.getString(1)) != null) {
                    if (TeamUtils.getTeamByName(cmd.getString(1)).isObserver())
                        throw new CommandException("You cannot rename observers!");
                    String msg = "";
                    for (int i = 2; i < cmd.argsLength(); i++) {
                        msg += cmd.getString(i) + " ";
                    }
                    msg = msg.trim();
                    TeamModule team = TeamUtils.getTeamByName(cmd.getString(1));
                    Bukkit.broadcastMessage(team.getCompleteName() + ChatColor.GRAY + " renamed to " + team.getColor() + msg);
                    team.setName(msg);
                    Bukkit.getServer().getPluginManager().callEvent(new ScoreboardUpdateEvent());
                } else {
                    throw new CommandException("Invalid team specified!");
                }
            } else {
                throw new CommandUsageException("Too few arguments!", "/team <alias> <old team> <new team>");
            }
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
        } else {
            throw new CommandUsageException("Invalid arguments.", "/team <force, alias, shuffle> [player, old team] [force team, new team]");
        }
    }
}
