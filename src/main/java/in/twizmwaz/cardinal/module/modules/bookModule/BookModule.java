package in.twizmwaz.cardinal.module.modules.bookModule;

import in.twizmwaz.cardinal.chat.ChatConstant;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.util.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BookModule implements Module {

    private List<ChatConstant> chatPages = Arrays.asList(ChatConstant.BOOK_PAGE_1, ChatConstant.BOOK_PAGE_2, ChatConstant.BOOK_PAGE_3, ChatConstant.BOOK_PAGE_4, ChatConstant.BOOK_PAGE_5, ChatConstant.BOOK_PAGE_6, ChatConstant.BOOK_PAGE_7, ChatConstant.BOOK_PAGE_8, ChatConstant.BOOK_PAGE_9, ChatConstant.BOOK_PAGE_10);
    private Match match;

    public BookModule(Match match) {
        this.match = match;
    }

    @Override
    public void unload() {
        HandlerList.unregisterAll(this);
    }

    public void giveBook(Player player, int slot) {
        ArrayList<String> pages = new ArrayList<String>();
        for (ChatConstant message : chatPages) {
            String msg = message.getMessage(player.getLocale());
            msg = ChatColor.translateAlternateColorCodes('`', msg.trim());
            if (msg == null || msg == "") {
                break;
            }
            pages.add(msg);
        }
        ItemStack howTo = ItemUtils.createBook(1, ChatColor.translateAlternateColorCodes('`', ChatConstant.BOOK_TITLE.getMessage(player.getLocale())), ChatColor.translateAlternateColorCodes('`', ChatConstant.BOOK_AUTHOR.getMessage(player.getLocale())), pages);
        player.getInventory().setItem(slot, howTo);
    }

}
