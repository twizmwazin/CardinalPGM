package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.module.modules.timers.StartTimer;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands {

    @Command(aliases = {"force"}, desc = "Forces a player onto the match.", usage = "<player>", min = 1)
    @CommandPermissions("cardinal.team.force")
    public static void force(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!Teams.isFFA()) throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYERS).getMessage(ChatUtil.getLocale(sender)));
        Player player = Bukkit.getPlayer(cmd.getString(0));
        if (player == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        TeamModule team = Teams.getPlayerManager();
        if (team.contains(player)) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_ALREADY_ON_TEAM, Players.getName(player, false) + ChatColor.RED, team.getCompleteName()).getMessage(ChatUtil.getLocale(sender)));
        }
        team.add(player, true, false);
        sender.sendMessage(ChatColor.GRAY + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_FORCE, Players.getName(player, false) + ChatColor.GRAY, team.getCompleteName() + ChatColor.GRAY).getMessage(ChatUtil.getLocale(sender)));

    }

    @Command(aliases = {"min"}, desc = "Changes the match's size.", usage = "<min>", min = 1, max = 1)
    @CommandPermissions("cardinal.team.size")
    public static void min(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!Teams.isFFA()) throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYERS).getMessage(ChatUtil.getLocale(sender)));
        TeamModule players = Teams.getPlayerManager();
        players.setMin(Integer.parseInt(cmd.getString(0)));
        sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SIZE_CHANGED, players.getCompleteName() + ChatColor.WHITE, ChatColor.AQUA + cmd.getString(0)).getMessage(ChatUtil.getLocale(sender)));
        GameHandler.getGameHandler().getMatch().getModules().getModule(StartTimer.class).updateNeededPlayers();
    }

    @Command(aliases = {"size", "max"}, desc = "Changes the specified match's size.", usage = "<size>", min = 1, max = 1)
    @CommandPermissions("cardinal.team.size")
    public static void size(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!Teams.isFFA()) throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYERS).getMessage(ChatUtil.getLocale(sender)));
        TeamModule players = Teams.getPlayerManager();
        players.setMax(Integer.parseInt(cmd.getString(0)));
        sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_TEAM_SIZE_CHANGED, players.getCompleteName() + ChatColor.WHITE, ChatColor.AQUA + cmd.getString(0)).getMessage(ChatUtil.getLocale(sender)));
    }

    public static class PlayersParentCommand {
        @Command(aliases = {"players","ffa"}, desc = "Manage the ffa module in the match.")
        @NestedCommand({PlayerCommands.class})
        public static void players(final CommandContext args, CommandSender sender) throws CommandException {

        }
    }

}
