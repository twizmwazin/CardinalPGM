package in.twizmwaz.cardinal.util;

import com.sk89q.minecraft.util.commands.CommandException;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.chat.ChatMessage;
import in.twizmwaz.cardinal.chat.LocalizedChatMessage;
import in.twizmwaz.cardinal.chat.UnlocalizedChatMessage;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.modules.chatChannels.AdminChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.GlobalChannel;
import in.twizmwaz.cardinal.module.modules.chatChannels.TeamChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatUtil {

    public static BaseComponent baseComponentFromArray(BaseComponent[] array) {
        BaseComponent result = new TextComponent("");
        for (BaseComponent component : array) {
            result.addExtra(component);
        }
        return result;
    }

    public static void sendWarningMessage(Player player, String msg) {
        if (msg != null) player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg);
    }

    public static void sendWarningMessage(Player player, ChatMessage msg) {
        if (msg != null)
            player.sendMessage(ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + msg.getMessage(player.getLocale()));
    }

    public static String getWarningMessage(String msg) {
        if (msg == null) return null;
        else return ChatColor.YELLOW + " \u26A0 " + ChatColor.RED + ChatColor.translateAlternateColorCodes('`', msg);
    }

    public static String getLocale(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getLocale() : Locale.getDefault().toString();
    }

    public static GlobalChannel getGlobalChannel() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(GlobalChannel.class);
    }

    public static AdminChannel getAdminChannel() {
        return GameHandler.getGameHandler().getMatch().getModules().getModule(AdminChannel.class);
    }

    public static ModuleCollection<TeamChannel> getTeamChannels() {
        return GameHandler.getGameHandler().getMatch().getModules().getModules(TeamChannel.class);
    }

    public static ChatColor getTimerColor(double time) {
        if (time <= 5) {
            return ChatColor.DARK_RED;
        } else if (time <= 30) {
            return ChatColor.GOLD;
        } else if (time <= 60) {
            return ChatColor.YELLOW;
        } else {
            return ChatColor.GREEN;
        }
    }

    public static ChatMessage toChatMessage(List<String> names) {
        return toChatMessage(names, ChatColor.RED, ChatColor.DARK_PURPLE);
    }

    public static ChatMessage toChatMessage(List<String> names, ChatColor nameColor, ChatColor extraColor) {
        int size = names.size();
        if (size == 1) {
            return new UnlocalizedChatMessage(nameColor + names.get(0));
        } else if (size > 1) {
            String first = "";
            for (String name : names) {
                int index = names.indexOf(name);
                if (index < size - 2) {
                    first += nameColor + name + extraColor + ", ";
                } else if (index == size - 2) {
                    first += nameColor + name + extraColor;
                } else if (index == size - 1) {
                    return new LocalizedChatMessage(ChatConstant.MISC_AND, first, nameColor + name + extraColor);
                }
            }
        }
        return new UnlocalizedChatMessage("");
    }

    /**
     * Paginates a list of objects and displays them to the sender
     *
     * @param sender     Who to show the paginated result.
     * @param header     The header shown as title.
     * @param index      Page index, what page the sender wants to see.
     * @param streamSize The size of the stream, can't get it from the steam because that would consume it.
     * @param pageSize   The size of each page (usually 8).
     * @param stream     The stream of objects to paginate.
     * @param toString   A function to convert the objects to chat messages.
     */
    public static <T> void paginate(CommandSender sender, ChatConstant header, int index, int streamSize, int pageSize,
                                    Stream<T> stream, Function<T, ChatMessage> toMessage, Function<T, String> toString) throws CommandException {
        paginate(sender, header, index, streamSize, pageSize, stream, toMessage, toString, -1, null);
    }

    public static <T> void paginate(CommandSender sender, ChatConstant header, int index, int streamSize, int pageSize,
                                    Stream<T> stream, Function<T, ChatMessage> toMessage, Function<T, String> toString,
                                    int mark, ChatColor markColor) throws CommandException {
        int pages = (int) Math.ceil((streamSize + (pageSize - 1)) / pageSize);
        List<String> page;
        try {
            int current = pageSize * (index - 1);
            page = new Indexer().index(toString(paginate(stream, pageSize, index), toMessage,
                    ChatUtil.getLocale(sender), toString), current, mark + 1, markColor).collect(Collectors.toList());
            if (page.size() == 0) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            throw new CommandException(new LocalizedChatMessage(ChatConstant.ERROR_INVALID_PAGE_NUMBER, pages + "")
                    .getMessage(ChatUtil.getLocale(sender)));
        }
        sender.sendMessage(Align.padMessage(new LocalizedChatMessage(header, Strings.page(index, pages)).getMessage(ChatUtil.getLocale(sender))));
        page.forEach(sender::sendMessage);
        sender.sendMessage(Align.getDash());
    }

    public static <T> Stream<String> toString(Stream<T> stream, Function<T, ChatMessage> toChatMessage,
                                              String locale, Function<T, String> toString) {
        if (toChatMessage != null) {
            return stream.map(toChatMessage).map(msg -> msg.getMessage(locale));
        } else {
            return stream.map(toString);
        }
    }

    public static <T> Stream<T> paginate(Stream<T> stream, int pageSize, int index) {
        return stream.skip(pageSize * (index - 1)).limit(pageSize);
    }

    public enum ChannelType {
        GLOBAL, ADMIN, TEAM
    }

    private static class Indexer {

        private int index;

        private Stream<String> index(Stream<String> stream, int index, int highlight, ChatColor markColor) {
            this.index = index;
            return stream.map(str -> {
                this.index++;
                return str.replace("${index}", "" + (this.index == highlight ? markColor : "") + this.index + "");
            });
        }

    }

}
