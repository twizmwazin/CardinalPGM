package in.twizmwaz.cardinal.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.repository.LoadedMap;
import in.twizmwaz.cardinal.repository.RepositoryManager;
import in.twizmwaz.cardinal.repository.repositories.Repository;
import in.twizmwaz.cardinal.util.Align;
import in.twizmwaz.cardinal.util.ChatUtil;
import in.twizmwaz.cardinal.util.Contributor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MapCommands {

    private static final String TITLE_FORM = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD;
    private static final String CONT_FORM = "" + ChatColor.RESET + ChatColor.GOLD;

    @Command(aliases = {"map", "mapinfo"}, flags = "lm:", desc = "Shows information about the currently playing map.")
    public static void map(final CommandContext args, CommandSender sender) throws CommandException {
        LoadedMap mapInfo = args.hasFlag('m') ? RepositoryManager.get().getMap(args.getFlagInteger('m')) :
                        args.argsLength() == 0 ? GameHandler.getGameHandler().getMatch().getLoadedMap() :
                                CycleCommand.getMap(sender, args.getJoinedStrings(0));
        if (mapInfo == null) 
            throw new CommandException(ChatConstant.ERROR_NO_MAP_MATCH.getMessage(ChatUtil.getLocale(sender)));
        sender.sendMessage(Align.padMessage(mapInfo.toShortMessage(ChatColor.DARK_AQUA, args.hasFlag('l'), true), ChatColor.RED));
        sender.sendMessage(TITLE_FORM + ChatConstant.UI_MAP_OBJECTIVE.getMessage(ChatUtil.getLocale(sender)) + ": "
                + CONT_FORM + mapInfo.getObjective());
        sendContributors(sender, ChatConstant.UI_MAP_AUTHOR, ChatConstant.UI_MAP_AUTHORS, mapInfo.getAuthors());
        sendContributors(sender, ChatConstant.UI_MAP_CONTRIBUTORS, ChatConstant.UI_MAP_CONTRIBUTORS, mapInfo.getContributors());
        if (mapInfo.getRules().size() > 0) {
            sender.sendMessage(TITLE_FORM + ChatConstant.UI_MAP_RULES.getMessage(ChatUtil.getLocale(sender)) + ":");
            for (int i = 1; i <= mapInfo.getRules().size(); i++)
                sender.sendMessage(ChatColor.WHITE + "" + i + ") " + CONT_FORM + mapInfo.getRules().get(i - 1));
        }
        sender.sendMessage(TITLE_FORM + ChatConstant.UI_MAP_MAX.getMessage(ChatUtil.getLocale(sender)) + ": "
                + CONT_FORM + mapInfo.getMaxPlayers());
        if (args.hasFlag('l')) {
            Repository repo = GameHandler.getGameHandler().getRepositoryManager().getRepo(mapInfo);
            if (repo != null) {
                sender.sendMessage(TITLE_FORM + "Source: " + repo.toChatMessage(sender.isOp()));
                sender.sendMessage(TITLE_FORM + "Folder: " + CONT_FORM +
                        repo.getRoot().toURI().relativize(mapInfo.getFolder().toURI()).getPath());
            } else {
                sender.sendMessage(TITLE_FORM + "Source: " + CONT_FORM + "Unknown");
            }
        }
    }

    private static void sendContributors(CommandSender sender, ChatConstant titleSing,
                                         ChatConstant titlePlur, List<Contributor> contributors) {
        if (contributors.size() == 0) return;
        if (contributors.size() == 1) {
            sender.sendMessage(TITLE_FORM + titleSing.getMessage(ChatUtil.getLocale(sender)) + ": " + contributors.get(0).toChatMessage());
            return;
        }
        sender.sendMessage(TITLE_FORM + titlePlur.getMessage(ChatUtil.getLocale(sender)) + ":");
        contributors.forEach(author -> sender.sendMessage("  " + author.toChatMessage()));
    }

}
