package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.module.modules.observers.ObserverModule;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Numbers;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommands {

    @Command(aliases = {"tp", "teleport"}, desc = "Teleport players.", usage = "<player> [player], [player] [[~]x] [[~]y] [[~]z]", min = 1, max = 4)
    public static void teleport(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.argsLength() == 1) {
                if (sender.hasPermission("cardinal.teleport") || ObserverModule.testObserver(player)) {
                    try {
                        Player target = Bukkit.getPlayer(cmd.getString(0));
                        player.teleport(target);
                        sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TELEPORTED).getMessage(player.getLocale()));
                    } catch (NullPointerException e) {
                        throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(player.getLocale()));
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else if (cmd.argsLength() == 2) {
                if (sender.hasPermission("cardinal.teleport")) {
                    try {
                        Player from = Bukkit.getPlayer(cmd.getString(0));
                        Player to = Bukkit.getPlayer(cmd.getString(1));
                        from.teleport(to);
                        sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TELEPORTED).getMessage(player.getLocale()));
                        if (player != from) {
                            from.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TELEPORTED_BY, player.getDisplayName()).getMessage((from.getLocale())));
                        }
                    } catch (NullPointerException e) {
                        throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(player.getLocale()));
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else if (cmd.argsLength() == 3) {
                if (sender.hasPermission("cardinal.teleport")) {
                    double x = cmd.getString(0).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(0).replaceAll("~", ""));
                    double y = cmd.getString(1).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(1).replaceAll("~", ""));
                    double z = cmd.getString(2).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(2).replaceAll("~", ""));
                    if (cmd.getString(0).contains("~")) x += player.getLocation().getX();
                    if (cmd.getString(1).contains("~")) y += player.getLocation().getY();
                    if (cmd.getString(2).contains("~")) z += player.getLocation().getZ();
                    if ( -30000000 < x && x < 30000000) {
                        if ( -30000000 < z && z < 30000000) {
                            player.teleport(new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch()));
                            sender.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TELEPORTED).getMessage(player.getLocale()));
                        } else {
                            TranslatableComponent numTooBig = new TranslatableComponent("commands.generic.num.tooBig");
                            numTooBig.addWith("" + z);
                            numTooBig.addWith("30000000");
                            numTooBig.setColor(net.md_5.bungee.api.ChatColor.RED);
                            player.sendMessage(numTooBig);
                        }
                    } else {
                        TranslatableComponent numTooBig = new TranslatableComponent("commands.generic.num.tooBig");
                        numTooBig.addWith("" + x);
                        numTooBig.addWith("30000000");
                        numTooBig.setColor(net.md_5.bungee.api.ChatColor.RED);
                        player.sendMessage(numTooBig);
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else if (cmd.argsLength() == 4) {
                if (sender.hasPermission("cardinal.teleport")) {
                    try {
                        Player teleporting = Bukkit.getPlayer(cmd.getString(0));
                        double x = cmd.getString(1).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(1).replaceAll("~", ""));
                        double y = cmd.getString(2).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(2).replaceAll("~", ""));
                        double z = cmd.getString(3).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(3).replaceAll("~", ""));
                        if (cmd.getString(1).contains("~")) x += teleporting.getLocation().getX();
                        if (cmd.getString(2).contains("~")) y += teleporting.getLocation().getY();
                        if (cmd.getString(3).contains("~")) z += teleporting.getLocation().getZ();
                        if ( -30000000 < x && x < 30000000) {
                            if ( -30000000 < z && z < 30000000) {
                                teleporting.teleport(new Location(teleporting.getWorld(), x, y, z, teleporting.getLocation().getYaw(), teleporting.getLocation().getPitch()));
                                sender.sendMessage(new LocalizedChatMessage(ChatConstant.GENERIC_TELEPORTED).getMessage(player.getLocale()) + ChatColor.YELLOW);
                                if (player != teleporting) {
                                    teleporting.sendMessage(ChatColor.YELLOW + new LocalizedChatMessage(ChatConstant.GENERIC_TELEPORTED_BY, player.getDisplayName()).getMessage((teleporting.getLocale())));
                                }
                            } else {
                                TranslatableComponent numTooBig = new TranslatableComponent("commands.generic.num.tooBig");
                                numTooBig.addWith("" + z);
                                numTooBig.addWith("30000000");
                                numTooBig.setColor(net.md_5.bungee.api.ChatColor.RED);
                                player.sendMessage(numTooBig);
                            }
                        } else {
                            TranslatableComponent numTooBig = new TranslatableComponent("commands.generic.num.tooBig");
                            numTooBig.addWith("" + x);
                            numTooBig.addWith("30000000");
                            numTooBig.setColor(net.md_5.bungee.api.ChatColor.RED);
                            player.sendMessage(numTooBig);
                        }
                    } catch (NullPointerException e) {
                        throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(player.getLocale()));
                    }
                } else {
                    throw new CommandPermissionsException();
                }
            } else {
                throw new CommandPermissionsException();
            }
        } else if (cmd.argsLength() == 2) {
            if (!sender.hasPermission("cardinal.teleport")) {
                throw new CommandPermissionsException();
            }
            try {
                Player from = Bukkit.getPlayer(cmd.getString(0));
                Player to = Bukkit.getPlayer(cmd.getString(1));
                from.teleport(to);
                sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TELEPORTED.getMessage(ChatUtil.getLocale(sender)));
            } catch (NullPointerException e) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(((Player) sender).getLocale()));
            }
        } else if (cmd.argsLength() == 3) {
            if (!sender.hasPermission("cardinal.teleport")) {
                throw new CommandPermissionsException();
            }
            double x = cmd.getString(0).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(0).replaceAll("~", ""));
            double y = cmd.getString(1).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(1).replaceAll("~", ""));
            double z = cmd.getString(2).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(2).replaceAll("~", ""));
            if (cmd.getString(0).contains("~")) x += ((Player) sender).getLocation().getX();
            if (cmd.getString(1).contains("~")) y += ((Player) sender).getLocation().getY();
            if (cmd.getString(2).contains("~")) z += ((Player) sender).getLocation().getZ();
            ((Player) sender).teleport(new Location(((Player) sender).getWorld(), x, y, z, ((Player) sender).getLocation().getYaw(), ((Player) sender).getLocation().getPitch()));
            sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TELEPORTED.getMessage(ChatUtil.getLocale(sender)));
        } else if (cmd.argsLength() == 4) {
            if (!sender.hasPermission("cardinal.teleport")) {
                throw new CommandPermissionsException();
            }
            try {
                Player teleporting = Bukkit.getPlayer(cmd.getString(0));
                double x = cmd.getString(1).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(1).replaceAll("~", ""));
                double y = cmd.getString(2).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(2).replaceAll("~", ""));
                double z = cmd.getString(3).equals("~") ? 0 : Numbers.parseDouble(cmd.getString(3).replaceAll("~", ""));
                if (cmd.getString(1).contains("~")) x += teleporting.getLocation().getX();
                if (cmd.getString(2).contains("~")) y += teleporting.getLocation().getY();
                if (cmd.getString(3).contains("~")) z += teleporting.getLocation().getZ();
                teleporting.teleport(new Location(teleporting.getWorld(), x, y, z, teleporting.getLocation().getYaw(), teleporting.getLocation().getPitch()));
                sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TELEPORTED.getMessage(ChatUtil.getLocale(sender)));
            } catch (NullPointerException e) {
                throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_NO_PLAYER_MATCH).getMessage(((Player) sender).getLocale()));
            }
        } else {
            throw new CommandUsageException(ChatConstant.ERROR_INVALID_ARGUMENTS.getMessage(ChatUtil.getLocale(sender)), "/teleport <player> [to], [x] [y] [z]");
        }
    }

    @Command(aliases = {"bring", "tphere", "grab"}, desc = "Teleport a player to you.", usage = "[player]", min = 1, max = 1)
    @CommandPermissions("cardinal.teleport")
    public static void teleportHere(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!(sender instanceof Player)) {
            throw new CommandException(ChatConstant.ERROR_CONSOLE_NO_USE.getMessage(ChatUtil.getLocale(sender)));
        }
        Player teleporting = Bukkit.getPlayer(cmd.getString(0));
        if (teleporting == null) {
            throw new CommandException(ChatConstant.ERROR_NO_PLAYER_MATCH.getMessage(ChatUtil.getLocale(sender)));
        }
        teleporting.teleport((Player) sender);
        sender.sendMessage(ChatColor.YELLOW + ChatConstant.GENERIC_TELEPORTED.getMessage(ChatUtil.getLocale(sender)));
    }

}
