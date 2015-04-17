package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.event.PlayerNameUpdateEvent;
import in.twizmwaz.cardinal.match.MatchState;
import in.twizmwaz.cardinal.module.modules.classModule.ClassModule;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.module.modules.permissions.PermissionModule;
import in.twizmwaz.cardinal.util.ItemUtils;
import in.twizmwaz.cardinal.util.TeamUtils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

public class LivestreamerCommand {
    @Command(aliases = {"live", "livestreamer"}, desc = "Give a player the rank of livestreamer", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.admin.live")
    public static void live(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer livestreamer = Bukkit.getOfflinePlayer(cmd.getString(0));
        if (livestreamer != null) {
            if (!livestreamer.isOp()) {
                if (!PermissionModule.isLivestreamer(livestreamer.getUniqueId()) && !PermissionModule.isMod(livestreamer.getUniqueId())) {
                    List<String> players = GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Livestreamer.players");
                    players.add(livestreamer.getUniqueId().toString());
                    GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.Livestreamer.players", players);
                    GameHandler.getGameHandler().getPlugin().saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "You gave livestreamer permissions to " + TeamUtils.getTeamColorByPlayer(livestreamer) + (livestreamer.isOnline() ? ((Player) livestreamer).getDisplayName() : livestreamer.getName()));
                    if (livestreamer.isOnline()) {
                        ((Player) livestreamer).sendMessage(ChatColor.GREEN + "You are now a livestreamer!");
                        for (String permission : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Livestreamer.permissions")) {
                            GameHandler.getGameHandler().getMatch().getModules().getModules(PermissionModule.class).get(0).enablePermission((Player) livestreamer, permission);
                        }
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent((Player) livestreamer));
                        if (livestreamer.isOnline()) {
                            if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                                ItemStack picker = ItemUtils.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                                        ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(((Player) livestreamer).getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(((Player) livestreamer).getLocale())),
                                        Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(((Player) livestreamer).getLocale())));
                                ((Player) livestreamer).getInventory().setItem(2, picker);
                            }
                        }
                    }
                } else {
                    throw new CommandException("The player specified is already a livestreamer!");
                }
            } else {
                throw new CommandException("The player specified is already op!");
            }
        } else {
            throw new CommandException("Unknown player specified!");
        }
    }

    @Command(aliases = {"delive", "delivestreamer", "unlive", "unlivestreamer"}, desc = "Remove a player from the rank of livestreamer", usage = "<player>", min = 1, max = 1)
    @CommandPermissions("cardinal.admin.delive")
    public static void demod(CommandContext cmd, CommandSender sender) throws CommandException {
        OfflinePlayer livestreamer = Bukkit.getOfflinePlayer(cmd.getString(0));
        if (livestreamer != null) {
            if (!livestreamer.isOp()) {
                if (PermissionModule.isLivestreamer(livestreamer.getUniqueId())) {
                    List<String> players = GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Livestreamer.players");
                    players.remove(livestreamer.getUniqueId().toString());
                    GameHandler.getGameHandler().getPlugin().getConfig().set("permissions.Livestreamer.players", players);
                    sender.sendMessage(ChatColor.RED + "You removed livestreamer permissions from " + TeamUtils.getTeamColorByPlayer(livestreamer) + (livestreamer.isOnline() ? ((Player) livestreamer).getDisplayName() : livestreamer.getName()));
                    if (livestreamer.isOnline()) {
                        ((Player) livestreamer).sendMessage(ChatColor.RED + "You are no longer a livestreamer!");
                        for (String permission : GameHandler.getGameHandler().getPlugin().getConfig().getStringList("permissions.Livestreamer.permissions")) {
                            GameHandler.getGameHandler().getMatch().getModules().getModules(PermissionModule.class).get(0).disablePermission((Player) livestreamer, permission);
                        }
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerNameUpdateEvent((Player) livestreamer));
                        if (livestreamer.isOnline()) {
                            if (!GameHandler.getGameHandler().getMatch().getState().equals(MatchState.ENDED)) {
                                ItemStack picker = ItemUtils.createItem(Material.LEATHER_HELMET, 1, (short) 0,
                                        ChatColor.GREEN + "" + ChatColor.BOLD + (GameHandler.getGameHandler().getMatch().getModules().getModule(ClassModule.class) != null ? new LocalizedChatMessage(ChatConstant.UI_TEAM_CLASS_SELECTION).getMessage(((Player) livestreamer).getLocale()) : new LocalizedChatMessage(ChatConstant.UI_TEAM_SELECTION).getMessage(((Player) livestreamer).getLocale())),
                                        Arrays.asList(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.UI_TEAM_JOIN_TIP).getMessage(((Player) livestreamer).getLocale())));
                                ((Player) livestreamer).getInventory().setItem(2, picker);
                            }
                        }
                    }
                } else {
                    throw new CommandException("The player specified is not a livestreamer!");
                }
            } else {
                throw new CommandException("The player specified is already op!");
            }
        } else {
            throw new CommandException("Unknown player specified!");
        }
    }
}
