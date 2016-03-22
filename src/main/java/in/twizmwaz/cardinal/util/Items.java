package in.twizmwaz.cardinal.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;
import java.util.List;

public class Items {

    public static ItemStack createItem(Material material, int amount, short data, String name) {
        return createItem(material, amount, data, name, null);
    }

    public static ItemStack createItem(Material material, int amount, short data, String name, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBook(Material material, int amount, String name, String author) {
        ItemStack item = createItem(material, amount, (short) 0, name);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(author);
        meta.setPages(Collections.singletonList(""));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createLeatherArmor(Material material, int amount, String name, List<String> lore, Color color) {
        ItemStack item = createItem(material, amount, (short) 0, name, lore);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean itemsEqual(ItemStack item1, ItemStack item2) {
        if (!item1.getType().equals(item2.getType())) return false;
        return toMaxDurability(item1).isSimilar(toMaxDurability(item2)) || CraftItemStack.asCraftCopy(item1).getHandle().getTag().equals(CraftItemStack.asCraftCopy(item2).getHandle().getTag());
    }

    public static ItemStack toMaxDurability(ItemStack item) {
        ItemStack item2 = item.clone();
        item2.setDurability((short)0);
        return item2;
    }

}
