package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import in.twizmwaz.cardinal.Cardinal;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.repository.LoadedMap;
import in.twizmwaz.cardinal.repository.RepositoryManager;
import in.twizmwaz.cardinal.repository.exception.RotationLoadException;
import in.twizmwaz.cardinal.util.AsyncCommand;
import in.twizmwaz.cardinal.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Locale;

public class RepositoryCommands {

    @Command(aliases = {"rotation", "rot", "rota", "maprot", "maprotation"}, flags = "l", desc = "Shows the current rotation.", usage = "[page]")
    public static void rotation(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtil.paginate(sender, ChatConstant.UI_ROTATION_CURRENT, cmd.getInteger(0, 1), RepositoryManager.get().getRotation().size(),
                8, RepositoryManager.get().getRotation().stream(), map -> map.toIndexedMessage(cmd.hasFlag('l')), null,
                RepositoryManager.get().getRotation().getNextIndex(), ChatColor.DARK_AQUA);
    }

    @Command(aliases = {"next", "nextmap", "nm", "mn", "mapnext"}, flags = "l", desc = "Shows next map.")
    public static void next(final CommandContext cmd, CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_PURPLE + new LocalizedChatMessage(ChatConstant.GENERIC_MAP_NEXT,
                GameHandler.getGameHandler().getCycle().getMap().toChatMessage(cmd.hasFlag('l')))
                .getMessage(ChatUtil.getLocale(sender)));
    }

    @Command(aliases = {"maps", "maplist", "ml"}, flags = "l", desc = "Shows all currently loaded maps.", usage = "[page]")
    public static void maps(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtil.paginate(sender, ChatConstant.UI_MAPLOADED, cmd.getInteger(0, 1), RepositoryManager.get().getMapSize(),
                8, RepositoryManager.get().getLoadedStream().sorted(Comparator.comparing(LoadedMap::getName)),
                map -> map.toIndexedMessage(cmd.hasFlag('l')), null);
    }

    @Command(aliases = {"repositories", "repos", "repo", "maprepo"}, desc = "Shows all currently loaded repos.", usage = "[page]")
    public static void repos(final CommandContext cmd, CommandSender sender) throws CommandException {
        ChatUtil.paginate(sender, ChatConstant.UI_REPOLOADED, cmd.getInteger(0, 1), RepositoryManager.get().getRepos().size(),
                8, RepositoryManager.get().getRepos().stream(), null, repo -> "${index}. " + repo.toChatMessage(sender.isOp()));
    }

    @Command(aliases = "newmaps", desc = "Reload map repository.")
    @CommandPermissions("cardinal.newmaps")
    public static void newMaps(final CommandContext cmd, CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(Cardinal.getInstance(), new AsyncCommand(cmd, sender) {
            @Override
            public void run() {
                try {
                    GameHandler.getGameHandler().getRepositoryManager().setupRotation();
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.GREEN + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_REPO_RELOAD,
                            "" + RepositoryManager.get().getMapSize())).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
                } catch (RotationLoadException e) {
                    e.printStackTrace();
                    Bukkit.getLogger().severe(new LocalizedChatMessage(ChatConstant.GENERIC_REPO_RELOAD_FAIL, "" + RepositoryManager.get().getMapSize()).getMessage(Locale.getDefault().toString()));
                    sender.sendMessage(new UnlocalizedChatMessage(ChatColor.RED + "{0}", new LocalizedChatMessage(ChatConstant.GENERIC_REPO_RELOAD_FAIL,
                            "" + RepositoryManager.get().getMapSize())).getMessage(sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString()));
                }
            }
        });
    }

}
