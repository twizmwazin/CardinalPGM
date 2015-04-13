package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.Cardinal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.jdom2.Document;
import org.jdom2.Element;

public class ItemUtils {

    public static ItemStack createItem(Material material, int amount, short data, String name) {
        return createItem(material, amount, data, name, null);
    }

    public static ItemStack createItem(Material material, int amount, short data, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBook(int amount, String name, String author, List<String> pages) {
        ItemStack item = createItem(Material.WRITTEN_BOOK, amount, (short) 0, name);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(author);
        meta.setPages(pages);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, int amount, String name, List<String> lore, Color color) {
        ItemStack item = createItem(material, amount, (short)0, name, lore);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getObserverBook(String locale) {
        Document doc = Cardinal.getLocaleHandler().getLocaleDocument(locale.split("_")[0]);
        
        Element book = doc.getRootElement().getChild("book");
        if (book == null) {
            return createBook(1, ChatColor.AQUA + "" + ChatColor.BOLD + "Coming soon", ChatColor.GOLD + "CardinalPGM", new ArrayList<String>());
        }
        String name = ChatColor.translateAlternateColorCodes('`', book.getChildText("title"));
        String authour = ChatColor.translateAlternateColorCodes('`', book.getChildText("author"));
        List<String> pages = new ArrayList<String>();
        for (Element page : book.getChild("pages").getChildren("page")) {
            pages.add(ChatColor.translateAlternateColorCodes('`', page.getTextNormalize()));
        }
        return createBook(1, name, authour, pages);
    }

}
