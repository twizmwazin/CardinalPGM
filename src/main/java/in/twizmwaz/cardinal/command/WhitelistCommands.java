package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.team.TeamModule;
import in.twizmwaz.cardinal.util.AsyncCommand;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Players;
import in.twizmwaz.cardinal.util.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Original code by:
 *
 * @author OvercastNetwork
 * @author MonsieurApple
 * @author Anxuiz
 * @author Ramsey
 *         <p/>
 *         https://github.com/rmsy/Whitelister
 *         <p/>
 *         Modified for use with CardinalPGM
 */
public class WhitelistCommands {
    @Command(aliases = {"reload", "rl"}, desc = "Reload the whitelist from a file.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.reload")
    public static void reload(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().reloadWhitelist();
        sender.sendMessage(ChatColor.GREEN + ChatConstant.GENERIC_WHITELIST_RELOADED.getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"toggle", "t"}, desc = "Toggle the whitelist.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.toggle")
    public static void toggle(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().setWhitelist(!Bukkit.getServer().hasWhitelist());
        sender.sendMessage(Bukkit.getServer().hasWhitelist() ? ChatColor.GREEN + ChatConstant.GENERIC_WHITELIST_ENABLED.getMessage(ChatUtil.getLocale(sender)) : ChatColor.RED + ChatConstant.GENERIC_WHITELIST_DISABLED.getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"on"}, desc = "Turn the whitelist on.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.on")
    public static void on(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().setWhitelist(true);
        sender.sendMessage(ChatColor.GREEN + ChatConstant.GENERIC_WHITELIST_ENABLED.getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"off"}, desc = "Turn the whitelist off.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.off")
    public static void off(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().setWhitelist(false);
        sender.sendMessage(ChatColor.RED + ChatConstant.GENERIC_WHITELIST_DISABLED.getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"status", "s"}, desc = "Get the status of whitelist.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.status")
    public static void status(final CommandContext args, final CommandSender sender) throws CommandException {
        Set<OfflinePlayer> whitelisted = Bukkit.getServer().getWhitelistedPlayers();

        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int onlineWhitelistedPlayers = 0;
        int whitelistedPlayers = whitelisted.size();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (whitelisted.contains(player)) {
                onlineWhitelistedPlayers++;
            }
        }

        sender.sendMessage(Bukkit.getServer().hasWhitelist() ? ChatColor.GREEN + ChatConstant.GENERIC_WHITELIST_ENABLED.getMessage(ChatUtil.getLocale(sender)) : ChatColor.RED + ChatConstant.GENERIC_WHITELIST_DISABLED.getMessage(ChatUtil.getLocale(sender)));
        //sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(onlinePlayers == 1 ? ChatConstant.GENERIC_PLAYER_ONLINE : ChatConstant.GENERIC_PLAYERS_ONLINE, "" + ChatColor.GOLD + onlinePlayers + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
        //sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(whitelistedPlayers == 1 ? ChatConstant.GENERIC_WHITELISTED_PLAYER_ONLINE : ChatConstant.GENERIC_WHITELISTED_PLAYERS_ONLINE, "" + ChatColor.GOLD + whitelistedPlayers + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
        //sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(onlineWhitelistedPlayers == 1 ? ChatConstant.GENERIC_WHITELISTED_PLAYER_OUT_OF_ONLINE : ChatConstant.GENERIC_WHITELISTED_PLAYERS_OUT_OF_ONLINE, "" + ChatColor.GOLD + onlineWhitelistedPlayers + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
        sender.sendMessage(ChatColor.GREEN + new UnlocalizedChatMessage("{0} {1}",
                new LocalizedChatMessage(onlinePlayers == 1 ? ChatConstant.GENERIC_WHITELIST_STATUS_1_SING : ChatConstant.GENERIC_WHITELIST_STATUS_1_PLUR, ChatColor.GOLD + "" + onlinePlayers + ChatColor.GREEN),
                new LocalizedChatMessage(onlineWhitelistedPlayers == 1 ? ChatConstant.GENERIC_WHITELIST_STATUS_2_SING : ChatConstant.GENERIC_WHITELIST_STATUS_2_PLUR, ChatColor.GOLD + "" + onlineWhitelistedPlayers + ChatColor.GREEN)).getMessage(ChatUtil.getLocale(sender)));
        sender.sendMessage(ChatColor.GREEN + new UnlocalizedChatMessage("{0} {1}",
                new LocalizedChatMessage(whitelistedPlayers == 1 ? ChatConstant.GENERIC_WHITELIST_STATUS_3_SING : ChatConstant.GENERIC_WHITELIST_STATUS_3_PLUR, ChatColor.GOLD + "" + whitelistedPlayers + ChatColor.GREEN),
                new LocalizedChatMessage(onlineWhitelistedPlayers == 1 ? ChatConstant.GENERIC_WHITELIST_STATUS_4_SING : ChatConstant.GENERIC_WHITELIST_STATUS_4_PLUR, ChatColor.GOLD + "" + onlineWhitelistedPlayers + ChatColor.GREEN)).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"add", "a"}, desc = "Add someone to the whitelist.", min = 1, max = 1)
    @CommandPermissions("cardinal.whitelist.add")
    public static void add(final CommandContext args, final CommandSender sender) throws CommandException {
        if (args.getString(0).startsWith("@")) {
            Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new AsyncCommand(args, sender) {
                @Override
                public void run() {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args.getString(0).substring(1));
                    player.setWhitelisted(true);
                    sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_ADD_WHITELIST, Players.getName(player) + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
                }
            });
        } else {
            OfflinePlayer player = matchSinglePlayer(sender, args.getString(0));
            player.setWhitelisted(true);
            sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_ADD_WHITELIST, Players.getName(player) + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"remove", "r"}, desc = "Remove someone from the whitelist.", min = 1, max = 1)
    @CommandPermissions("cardinal.whitelist.remove")
    public static void remove(final CommandContext args, final CommandSender sender) throws CommandException {
        if (args.getString(0).startsWith("@")) {
            Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new AsyncCommand(args, sender) {
                @Override
                public void run() {
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args.getString(0).substring(1));
                    player.setWhitelisted(false);
                    sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_REMOVE_WHITELIST, Players.getName(player) + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
                }
            });
        } else {
            OfflinePlayer player = matchSinglePlayer(sender, args.getString(0));
            player.setWhitelisted(false);
            sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_PLAYER_REMOVE_WHITELIST, Players.getName(player) + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"all"}, desc = "Add everyone that's online to the whitelist.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.all")
    public static void all(final CommandContext args, final CommandSender sender) throws CommandException {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setWhitelisted(true);
        }
        sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_ADDED_PLAYERS_WHITELIST, "" + ChatColor.GOLD + Bukkit.getOnlinePlayers().size() + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"list", "l"}, desc = "List players on the whitelist.", min = 0, max = 1)
    @CommandPermissions("cardinal.whitelist.list")
    public static void list(final CommandContext args, final CommandSender sender) throws CommandException {
        sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "--------" + ChatColor.LIGHT_PURPLE + " {0} " + ChatColor.RED + ChatColor.STRIKETHROUGH + "--------", ChatConstant.GENERIC_WHITELISTED_PLAYERS.asMessage()).getMessage(ChatUtil.getLocale(sender)));
        if (Bukkit.getWhitelistedPlayers().size() != 0) {
            String online = "", offline = "";
            for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
                if (player.isOnline()) online += Players.getName(player) + ChatColor.RESET + " ";
                else offline += Players.getName(player) + ChatColor.RESET + " ";
            }
            sender.sendMessage(ChatColor.GREEN + new UnlocalizedChatMessage("{0}:", ChatConstant.MISC_ONLINE.asMessage()).getMessage(ChatUtil.getLocale(sender)));
            sender.sendMessage(online);
            sender.sendMessage(ChatColor.RED + new UnlocalizedChatMessage("{0}:", ChatConstant.MISC_OFFLINE.asMessage()).getMessage(ChatUtil.getLocale(sender)));
            sender.sendMessage(offline);
        } else {
            sender.sendMessage(ChatColor.RED + ChatConstant.GENERIC_NO_WHITELISTED_PLAYERS.getMessage(ChatUtil.getLocale(sender)));
        }
    }

    @Command(aliases = {"clear"}, desc = "Clear the whitelist.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.clear")
    public static void clear(final CommandContext args, final CommandSender sender) throws CommandException {
        int count = 0;
        for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
            player.setWhitelisted(false);
            count++;
        }
        sender.sendMessage(ChatColor.RED + new LocalizedChatMessage(ChatConstant.GENERIC_REMOVED_PLAYERS_WHITELIST, "" + ChatColor.GOLD + count + ChatColor.RED).getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"kick"}, desc = "Kicks everyone who is not on the whitelist.", min = 0, max = 0)
    @CommandPermissions("cardinal.whitelist.kick")
    public static void kick(final CommandContext args, final CommandSender sender) throws CommandException {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isWhitelisted() && !player.isOp() && !player.hasPermission("cardinal.whitelist.bypass")) {
                player.kickPlayer(ChatColor.RED + ChatConstant.GENERIC_KICKED_NOT_WHITELISTED.getMessage(ChatUtil.getLocale(player)));
            }
        }
        ChatUtil.getGlobalChannel().sendLocalizedMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", ChatConstant.GENERIC_KICKED_NOT_WHITELISTED.asMessage()));
    }

    @Command(aliases = {"team"}, desc = "Adds everyone on a team to the whitelist.", min = 1)
    @CommandPermissions("cardinal.whitelist.team")
    public static void team(final CommandContext args, final CommandSender sender) throws CommandException {
        int count = 0;
        List<String> teams = new ArrayList<>();
        String msg = args.getJoinedStrings(0).toLowerCase().replace(" ","");
        for (TeamModule team : Teams.getTeams()) {
            if (team.getName().toLowerCase().replace(" ","").startsWith(msg)) {
                teams.add(team.getCompleteName());
            }
        }
        if (teams.size() == 0) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_TEAM_MATCH).getMessage(ChatUtil.getLocale(sender)));
        } else if (teams.size() == 1) {
            TeamModule team = Teams.getTeamByName(teams.get(0).substring(2)).get();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (team.contains(player) && !player.isWhitelisted()) {
                    player.setWhitelisted(true);
                    count++;
                }
            }
            sender.sendMessage(ChatColor.GREEN + new LocalizedChatMessage(ChatConstant.GENERIC_ADDED_PLAYERS_WHITELIST, "" + ChatColor.GOLD + count + ChatColor.GREEN).getMessage(ChatUtil.getLocale(sender)));
        } else {
            String joinedTeams = "";
            for (int i = 0; i < teams.size() - 1; i++) {
                joinedTeams += teams.get(i) + ChatColor.RED + ", ";
            }
            joinedTeams += teams.get(teams.size() - 1) + ChatColor.RED + ".";
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_MULTIPLE_TEAM_MATCH, joinedTeams).getMessage(ChatUtil.getLocale(sender)));
        }
    }

    public static OfflinePlayer matchSinglePlayer(CommandSender sender, String rawUsername) throws CommandException {
        if (rawUsername.startsWith("@")) {
            return Bukkit.getOfflinePlayer(rawUsername.substring(1));
        } else {
            // look up player according to the who is online now
            List<Player> players = Bukkit.getServer().matchPlayer(rawUsername);
            switch (players.size()) {
                case 0:
                    throw new CommandException("No players matched query. Use @<name> for offline lookup.");
                case 1:
                    return players.get(0);
                default:
                    throw new CommandException("More than one player found! Use @<name> for exact matching.");
            }
        }
    }

    public static class WhitelistParentCommand {
        @Command(
                aliases = {"whitelist", "wl"},
                desc = "Commands for managing the whitelist.",
                min = 1
        )
        @NestedCommand({WhitelistCommands.class})
        public static void whitelist(final CommandContext args, CommandSender sender) throws CommandException {
        }
    }
}
