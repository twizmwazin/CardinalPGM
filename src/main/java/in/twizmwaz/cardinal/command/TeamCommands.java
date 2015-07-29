package in.twizmwaz.cardinal.command;

import com.google.common.base.Optional;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.event.TeamNameChangeEvent;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamCommands {

    @Command(aliases = {"force"}, desc = "Forces a player onto the team specified.", usage = "<player> <team>", min = 2)
    @CommandPermissions("cardinal.team.force")
    public static void force(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (Bukkit.getPlayer(cmd.getString(0)) != null) {
            Optional<TeamModule> team = Teams.getTeamByName(cmd.getJoinedStrings(1));
            if (team.isPresent()) {
                if (!team.get().contains(Bukkit.getPlayer(cmd.getString(0)))) {
                    team.get().add(Bukkit.getPlayer(cmd.getString(0)), true, false);
                    sender.sendMessage(team.get().getColor() + Bukkit.getPlayer(cmd.getString(0)).getName() + ChatColor.GRAY + " forced to " + team.get().getCompleteName());
                } else
                    throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_ON_TEAM,
                            Teams.getTeamByPlayer(Bukkit.getPlayer(cmd.getString(0))).get().getColor() + Bukkit.getPlayer(cmd.getString(0)).getName() + ChatColor.RED,
                            Teams.getTeamByPlayer(Bukkit.getPlayer(cmd.getString(0))).get().getCompleteName()).getMessage(((Player) sender).getLocale()));
            } else {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtil.getLocale(sender)));
            }
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"alias"}, desc = "Renames a the team specified.", usage = "<team> <name>", min = 2)
    @CommandPermissions("cardinal.team.alias")
    public static void alias(final CommandContext cmd, CommandSender sender) throws CommandException {
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
        if (team.isPresent()) {
            String msg = cmd.getJoinedStrings(1);
            String locale = ChatUtil.getLocale(sender);
            sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_ALIAS, team.get().getCompleteName() + ChatColor.GRAY, team.get().getColor() + msg + ChatColor.GRAY).getMessage(locale));
            team.get().setName(msg);
            Bukkit.getServer().getPluginManager().callEvent(new TeamNameChangeEvent(team.get()));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"shuffle"}, desc = "Shuffles the teams.")
    @CommandPermissions("cardinal.team.shuffle")
    public static void shuffle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().getState().equals(MatchState.WAITING) || GameHandler.getGameHandler().getMatch().getState().equals(MatchState.STARTING)) {
            List<Player> playersToShuffle = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Teams.getTeamByPlayer(player).isPresent()) {
                    if (!Teams.getTeamByPlayer(player).get().isObserver()) {
                        playersToShuffle.add(player);
                        TeamModule observers = Teams.getTeamById("observers").get();
                        observers.add(player, true, false);
                    }
                }
            }
            while (playersToShuffle.size() > 0) {
                Player player = playersToShuffle.get(new Random().nextInt(playersToShuffle.size()));
                Optional<TeamModule> team = Teams.getTeamWithFewestPlayers(GameHandler.getGameHandler().getMatch());
                if (team.isPresent()) team.get().add(player, true);
                playersToShuffle.remove(player);
            }
            String locale = ChatUtil.getLocale(sender);
            sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SHUFFLE).getMessage(locale));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_SHUFFLE).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"size"}, desc = "Changes the specified team's size.", usage = "<team> <size>", min = 2)
    @CommandPermissions("cardinal.team.size")
    public static void size(final CommandContext cmd, CommandSender sender) throws CommandException {
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
        if (team.isPresent()) {
            team.get().setMaxOverfill(Integer.parseInt(cmd.getString(1)));
            team.get().setMax(Integer.parseInt(cmd.getString(1)));
            sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SIZE_CHANGED, Teams.getTeamByName(cmd.getString(0)).get().getCompleteName() + ChatColor.WHITE, ChatColor.AQUA + cmd.getString(1)).getMessage(ChatUtil.getLocale(sender)));
        } else {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"myteam", "mt"}, desc = "Shows what team you are on", min = 0, max = 0)
    public static void myTeam(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            Optional<TeamModule> team = Teams.getTeamByPlayer((Player) sender);
            if (team.isPresent()) {
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_ON_TEAM, team.get().getCompleteName())).getMessage(((Player) sender).getLocale()));
            }
        }
    }

    public static class TeamParentCommand {
        @Command(aliases = {"team"}, desc = "Manage the teams in the match.")
        @NestedCommand({TeamCommands.class})
        public static void team(final CommandContext args, CommandSender sender) throws CommandException {

        }
    }
}
