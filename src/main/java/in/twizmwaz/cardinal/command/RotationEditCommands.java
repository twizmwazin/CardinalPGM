package in.twizmwaz.cardinal.command;

import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.rotation.LoadedMap;
import in.twizmwaz.cardinal.rotation.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.ChatUtils;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.NestedCommand;

public class RotationEditCommands {

    @Command(aliases = { "reload", "rl" }, desc = "Reloads the repository and rotation (with flag -r).", flags = "r")
    @CommandPermissions("cardinal.rotation.reload")
    public static void reload(final CommandContext args, final CommandSender sender) throws CommandException {
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new Runnable() {
            
            @Override
            public void run() {
                try {
                    GameHandler.getGameHandler().getRotation().refreshRepo();
                    sender.sendMessage(ChatColor.GREEN + ChatConstant.GENERIC_REPOSITORY_RELOAD.getMessage(ChatUtils.getLocale(sender)));
                } catch (RotationLoadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        
        if (args.hasFlag('r')) {
            Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new Runnable() {
                
                @Override
                public void run() {
                    try {
                        GameHandler.getGameHandler().getRotation().refreshRotation();
                        sender.sendMessage(ChatColor.GREEN + ChatConstant.GENERIC_ROTATION_RELOAD.getMessage(ChatUtils.getLocale(sender)));
                    } catch (RotationLoadException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Command(aliases = { "append", "a" }, desc = "Appends a map to the end of the rotation.")
    @CommandPermissions("cardinal.rotation.append")
    public static void append(final CommandContext args, final CommandSender sender) throws CommandException {
        LoadedMap map = GameHandler.getGameHandler().getRotation().getMap(args.getJoinedStrings(0));
        if (map == null)
            throw new CommandException("Invalid map " + args.getJoinedStrings(0));
        GameHandler.getGameHandler().getRotation().getRotation().add(map);
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_ROTATION_APPEND, ChatColor.GOLD + map.getName() + ChatColor.DARK_PURPLE).getMessage(ChatUtils.getLocale(sender)));
    }

    @Command(aliases = { "insert", "i" }, desc = "Inserts a map in to the the rotation.")
    @CommandPermissions("cardinal.rotation.insert")
    public static void insert(final CommandContext args, final CommandSender sender) throws CommandException {
        int index = args.getInteger(0);
        LoadedMap map = GameHandler.getGameHandler().getRotation().getMap(args.getJoinedStrings(1));
        if (map == null)
            throw new CommandException("Invalid map " + args.getJoinedStrings(1));
        GameHandler.getGameHandler().getRotation().getRotation().add(index - 1, map);
        if (GameHandler.getGameHandler().getRotation().getNextIndex() == (index - 1)) {
            GameHandler.getGameHandler().getCycle().setMap(map);
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_INSTERTED_INDEX, ChatColor.GOLD + map.getName() + ChatColor.DARK_PURPLE, index + "").getMessage(ChatUtils.getLocale(sender)));
    }

    @Command(aliases = { "remove", "r" }, desc = "Removes all instances of a map from the rotation.")
    @CommandPermissions("cardinal.rotation.remove")
    public static void remove(final CommandContext args, final CommandSender sender) throws CommandException {
        LoadedMap map = GameHandler.getGameHandler().getRotation().getMap(args.getJoinedStrings(0));
        if (map == null)
            throw new CommandException("Invalid map " + args.getJoinedStrings(1));
        for (LoadedMap loadedMap : GameHandler.getGameHandler().getRotation().getRotation()) {
            if (map.getName().equalsIgnoreCase(map.getName())) {
                GameHandler.getGameHandler().getRotation().getRotation().remove(loadedMap);
            }
        }
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_REMOVED_INSTANCE, ChatColor.GOLD + map.getName() + ChatColor.DARK_PURPLE).getMessage(ChatUtils.getLocale(sender)));
    }

    @Command(aliases = { "removeat", "ra" }, desc = "Removes a map at a certain index from the rotation.")
    @CommandPermissions("cardinal.rotation.removeat")
    public static void removeAt(final CommandContext args, final CommandSender sender) throws CommandException {
        int index = args.getInteger(0);
        GameHandler.getGameHandler().getRotation().getRotation().remove(index - 1);
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_REMOVED_MAP, index + "").getMessage(ChatUtils.getLocale(sender)));
    }

    public static class RotationEditParentCommand {

        @Command(aliases = { "rotationedit", "editrotation", "erot", "rote", "editrot", "rotedit" }, desc = "Manage the rotation.")
        @NestedCommand({ RotationEditCommands.class })
        public static void rotationEdit(final CommandContext args, final CommandSender sender) throws CommandException {

        }

    }

}
