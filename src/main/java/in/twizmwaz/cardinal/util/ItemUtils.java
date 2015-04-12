package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.bookModule.BookModule;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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

    public static void giveObserversBook(Player player, int slot) {
        GameHandler.getGameHandler().getMatch().getModules().getModule(BookModule.class).giveBook(player, slot);
    }

}
