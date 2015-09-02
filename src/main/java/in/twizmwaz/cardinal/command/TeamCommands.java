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
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.teamRegister.TeamRegisterModule;
import in.twizmwaz.cardinal.util.Authenticator;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TeamCommands {

    @Command(aliases = {"force"}, desc = "Forces a player onto the team specified.", usage = "<player> <team>", min = 2)
    @CommandPermissions("cardinal.team.force")
    public static void force(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getJoinedStrings(1));
        if (!team.isPresent()) {
            throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        if (team.get().contains(player)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_ON_TEAM, Players.getName(player, false) + ChatColor.RED, team.get().getCompleteName()).getMessage(ChatUtil.getLocale(sender)));
        }
        team.get().add(player, true, false);
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_FORCE, Players.getName(player, false) + ChatColor.GRAY, team.get().getCompleteName() + ChatColor.GRAY).getMessage(ChatUtil.getLocale(sender)));

    }

    @Command(aliases = {"alias"}, desc = "Renames a the team specified.", usage = "<team> <name>", min = 2)
    @CommandPermissions("cardinal.team.alias")
    public static void alias(final CommandContext cmd, CommandSender sender) throws CommandException {
        Optional<TeamModule> team = Teams.getTeamByName(cmd.getString(0));
        if (!team.isPresent()) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtil.getLocale(sender)));
        }
        String msg = cmd.getJoinedStrings(1);
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_ALIAS, team.get().getCompleteName() + ChatColor.GRAY, team.get().getColor() + msg + ChatColor.GRAY)));
        team.get().setName(msg);
        Bukkit.getServer().getPluginManager().callEvent(new TeamNameChangeEvent(team.get()));
    }

    @Command(aliases = {"shuffle"}, desc = "Shuffles the teams.")
    @CommandPermissions("cardinal.team.shuffle")
    public static void shuffle(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (GameHandler.getGameHandler().getMatch().isWaiting() || GameHandler.getGameHandler().getMatch().isStarting()) {
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
        if (!team.isPresent()) {
            throw new CommandException(ChatConstant.ERROR_NO_TEAM_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        team.get().setMaxOverfill(Integer.parseInt(cmd.getString(1)));
        team.get().setMax(Integer.parseInt(cmd.getString(1)));
        sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SIZE_CHANGED, team.get().getCompleteName() + ChatColor.WHITE, ChatColor.AQUA + cmd.getString(1)).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"myteam", "mt"}, desc = "Shows what team you are on", min = 0, max = 0)
    public static void myTeam(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Optional<TeamModule> team = Teams.getTeamByPlayer((Player) sender);
        if (team.isPresent()) {
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GRAY + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_ON_TEAM, team.get().getCompleteName())).getMessage(((Player) sender).getLocale()));
        }
    }
    @Command(aliases = {"register"}, desc = "Registers an OCN team to play a match", min = 1, max = 1, usage = "<ocnteam>")
    @CommandPermissions("cardinal.register")
    public static void register(CommandContext cmd, CommandSender sender) throws CommandException {
        String ocnteam = cmd.getString(0);
        TeamModule usedTeam = null;
        ArrayList registeredTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getRegisteredTeams();
        Map<String, TeamModule> usedTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getUsedTeams();
        if (registeredTeams.contains(ocnteam)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_TEAM_ALREADY_REGISTERED).getMessage(ChatUtil.getLocale(sender)));
        }
        if((Teams.getTeams().size() - 1 == usedTeams.size())) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CANT_REGISTER_MORE_TEAMS).getMessage(ChatUtil.getLocale(sender)));
        }
        for (TeamModule team : Teams.getTeams()) {
            if (!team.isObserver() && !usedTeams.containsKey(team.getName())) {
                usedTeam = team;
                try {
                    Authenticator.downloadTeamDocument(ocnteam);
                } catch (IOException e) {
                    throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CANT_GET_TEAM_LIST).getMessage(ChatUtil.getLocale(sender)));
                }
                usedTeams.put(ocnteam, team);
                usedTeam.setName(ocnteam);
                Bukkit.getPluginManager().callEvent(new TeamNameChangeEvent(usedTeam));
                registeredTeams.add(ocnteam);
                sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}",  new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_REGISTERED, ChatColor.GOLD + ocnteam)).getMessage(ChatUtil.getLocale(sender)));
                for(Player p : Bukkit.getOnlinePlayers()) {
                    GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).addRegisteredPlayer(p.getName(), ocnteam);
                }
                return;
            }
        }
    }
    @Command(aliases = {"unregister"}, desc = "Unregisters an OCN team on match end", min = 1, max = 1, usage = "<ocnteam>")
    @CommandPermissions("cardinal.register")
    public static void unregister(CommandContext cmd, CommandSender sender) throws CommandException {
        String team = cmd.getString(0);
        ArrayList<String> registeredTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getRegisteredTeams();
        Map<String, TeamModule> usedTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getUsedTeams();
        Map<String, String> playersTeams = GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).getPlayersTeams();
        if(usedTeams.containsKey(team)) {
            for(String playerName : playersTeams.keySet()) {
                if(playersTeams.get(playerName) == team) {
                    Player player = Bukkit.getPlayer(playerName);
                    GameHandler.getGameHandler().getMatch().getModules().getModule(TeamRegisterModule.class).removeRegisteredPlayer(player);
                }
            }
            usedTeams.remove(team);
            registeredTeams.remove(team);
            sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}",  new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_UNREGISTERED, ChatColor.GOLD + team)).getMessage(ChatUtil.getLocale(sender)));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for(String ocnteam : registeredTeams) {
                stringBuilder.append(ChatColor.GOLD + " " +  ocnteam + ChatColor.RED + ",");
            }
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_CANT_FIND_ANY_REGISTERED_TEAM, stringBuilder.toString()).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    public static class TeamParentCommand {
        @Command(aliases = {"team"}, desc = "Manage the teams in the match.")
        @NestedCommand({TeamCommands.class})
        public static void team(final CommandContext args, CommandSender sender) throws CommandException {

        }
    }
}
