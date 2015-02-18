package in.twizmwaz.cardinal.command;

import java.util.List;
import java.util.Set;

import com.sk89q.minecraft.util.commands.*;
import in.twizmwaz.cardinal.util.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;


/**
 * Original code by:
 * 
 * @author OvercastNetwork
 * @author MonsieurApple
 * @author Anxuiz
 * @author Ramsey
 *
 * https://github.com/rmsy/Whitelister
 *
 * Modified for use with CardinalPGM
 *
 */
public class WhitelistCommands {
    public static class WhitelistParentCommand {
        @Command(
                aliases = { "whitelist", "wl" },
                desc = "Commands for managing the whitelist",
                min = 1,
                max = -1
        )
        @NestedCommand({WhitelistCommands.class})
        public static void whitelist(final CommandContext args, CommandSender sender) throws CommandException {
        }
    }

    @Command(
            aliases = { "reload", "rl" },
            desc = "Reload whitelist from a file",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.reload")
    public static void reload(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().reloadWhitelist();
        sender.sendMessage(ChatColor.GREEN + "Whitelist reloaded");
    }

    @Command(
            aliases = { "toggle", "t" },
            desc = "Toggle the whitelist",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.toggle")
    public static void toggle(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().setWhitelist(!Bukkit.getServer().hasWhitelist());
        sender.sendMessage(ChatColor.WHITE + "Whitelist: " + (Bukkit.getServer().hasWhitelist() ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
    }

    @Command(
            aliases = { "on" },
            desc = "Turn the whitelist on",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.on")
    public static void on(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().setWhitelist(true);
        sender.sendMessage(ChatColor.WHITE + "Whitelist: " + ChatColor.GREEN + "ON");
    }

    @Command(
            aliases = { "off" },
            desc = "Turn the whitelist off",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.off")
    public static void off(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getServer().setWhitelist(false);
        sender.sendMessage(ChatColor.WHITE + "Whitelist: " + ChatColor.RED + "OFF");
    }

    @Command(
            aliases = { "status", "s" },
            desc = "Get status of whitelist",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.status")
    public static void status(final CommandContext args, final CommandSender sender) throws CommandException {
        Set<OfflinePlayer> whitelisted = Bukkit.getServer().getWhitelistedPlayers();

        int whitelistedPlayers = whitelisted.size();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        int onlineWhitelistedPlayers = 0;


        for(OfflinePlayer player : Bukkit.getOnlinePlayers()) {
            if(whitelisted.contains(player)) {
                onlineWhitelistedPlayers++;
            }
        }

        sender.sendMessage(ChatColor.GOLD + "Whitelist Status: " + (Bukkit.getServer().hasWhitelist() ? ChatColor.GREEN  + "ON" : ChatColor.RED + "OFF"));
        sender.sendMessage(ChatColor.GREEN + "There " + (onlinePlayers == 1 ? "is" : "are") + " currently " + ChatColor.RED + String.valueOf(onlinePlayers) + ChatColor.GREEN + " player" + (onlinePlayers == 1 ? "" : "s") + " online");
        sender.sendMessage(ChatColor.GREEN + "There " + (whitelistedPlayers == 1 ? "is" : "are") + " currently " + ChatColor.RED + String.valueOf(whitelistedPlayers) + ChatColor.GREEN + " whitelisted player" + (whitelistedPlayers == 1 ? "" : "s"));
        sender.sendMessage(ChatColor.GREEN + "Of the online players, " + ChatColor.RED + String.valueOf(onlineWhitelistedPlayers) + ChatColor.GREEN + (onlineWhitelistedPlayers == 1 ? " is" : " are") + " whitelisted");
    }

    @Command(
            aliases = { "add", "a" },
            desc = "Add someone to the whitelist",
            min = 1,
            max = 1
    )
    @CommandPermissions("whitelist.add")
    public static void add(final CommandContext args, final CommandSender sender) throws CommandException {
        OfflinePlayer player = matchSinglePlayer(sender, args.getString(0));
        player.setWhitelisted(true);
        sender.sendMessage(ChatColor.GREEN + "Added " + ChatColor.GOLD + player.getName() + ChatColor.GREEN +" to the whitelist");
    }

    @Command(
            aliases = { "remove", "r" },
            desc = "Remove someone from the whitelist",
            min = 1,
            max = 1
    )
    @CommandPermissions("whitelist.remove")
    public static void remove(final CommandContext args, final CommandSender sender) throws CommandException {
        OfflinePlayer player = matchSinglePlayer(sender, args.getString(0));
        player.setWhitelisted(false);
        sender.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.GOLD + player.getName() + ChatColor.GREEN +" from the whitelist");
    }

    @Command(
            aliases = { "all" },
            desc = "Add everyone that's online to the whitelist",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.all")
    public static void all(final CommandContext args, final CommandSender sender) throws CommandException {
        int count = 0;
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setWhitelisted(true);
            count++;
        }
        sender.sendMessage(ChatColor.GREEN + "Added " + ChatColor.RED + String.valueOf(count) + ChatColor.GREEN + " player(s) to the whitelist");
    }

    @Command(
            aliases = { "list", "l" },
            desc = "List players on the whitelist",
            min = 0,
            max = 1
    )
    @CommandPermissions("whitelist.list")
    public static void list(final CommandContext args, final CommandSender sender) throws CommandException {
        sender.sendMessage(ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "-------" + ChatColor.LIGHT_PURPLE + "Whitelisted players:" + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "-------");
        if (Bukkit.getWhitelistedPlayers().size() != 0) {
            String onlineWhitelisted = ChatColor.GREEN + "Online: \n";
            String offlineWhitelisted = ChatColor.RED + "Offline: \n";
            for (OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
                if (player.isOnline()) {
                    if (TeamUtils.getTeamByPlayer(player.getPlayer()) != null) {
                        onlineWhitelisted += TeamUtils.getTeamByPlayer(player.getPlayer()).getColor() + player.getName() + ChatColor.RESET + " ";
                    }
                } else {
                    offlineWhitelisted += player.getName() + " ";
                }
            }
            sender.sendMessage(onlineWhitelisted);
            sender.sendMessage("");
            sender.sendMessage(offlineWhitelisted);
        } else {
            sender.sendMessage(ChatColor.RED + "There is currently no whitelisted players");
        }
    }

    @Command(
            aliases = { "clear" },
            desc = "Clear the whitelist",
            min = 0,
            max = 0
    )
    @CommandPermissions("whitelist.clear")
    public static void clear(final CommandContext args, final CommandSender sender) throws CommandException {
        int count = 0;
        for(OfflinePlayer player : Bukkit.getWhitelistedPlayers()) {
            player.setWhitelisted(false);
            count++;
        }
        sender.sendMessage(ChatColor.GREEN + "Removed " + ChatColor.RED + String.valueOf(count) + ChatColor.GREEN + " player(s) from the whitelist");
    }

    @Command(
            aliases = { "kick" },
            desc = "Kicks everyone who is not on the whitelist",
            max = 0,
            min = 0
    )
    @CommandPermissions("whitelist.kick")
    public static void kick(final CommandContext args, final CommandSender sender) throws CommandException {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isWhitelisted() && !player.isOp()) {
                player.kickPlayer("All players who were not on the whitelist were kicked");
            }
        }
    }

    @Command(
            aliases = { "team" },
            desc = "Adds everyone on a team to the whitelist",
            max = 1,
            min = 1
    )
    @CommandPermissions("whitelist.team")
    public static void team(final CommandContext args, final CommandSender sender) throws CommandException {
        int count = 0;
        String msg = "";
        for (int i = 2; i < args.argsLength(); i++) {
            msg += args.getString(i) + " ";
        }
        msg = msg.trim();
        if (TeamUtils.getTeamByName(msg) != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (TeamUtils.getTeamByPlayer(player).getName().startsWith(msg)) {
                    if (!player.isWhitelisted()) {
                        player.setWhitelisted(true);
                        count++;
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Added " + ChatColor.RED + String.valueOf(count) + ChatColor.GREEN + " player(s) to the whitelist");
            }
        }
    }

    public static OfflinePlayer matchSinglePlayer(CommandSender sender, String rawUsername) throws CommandException {
        if(rawUsername.startsWith("@")) {
            return Bukkit.getServer().getOfflinePlayer(rawUsername.substring(1));
        } else {
            // look up player according to the who is online now
            List<Player> players = Bukkit.getServer().matchPlayer(rawUsername);
            switch(players.size()) {
                case 0:
                    throw new CommandException("No players matched query. Use @<name> for offline lookup.");
                case 1:
                    return players.get(0);
                default:
                    throw new CommandException("More than one player found! Use @<name> for exact matching.");
            }
        }
    }
}
