package in.twizmwaz.cardinal.util;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Element;

import java.util.ArrayList;

public class ParseUtils {
    
    public static ItemStack getItem(Element element) {
        int amount = 1;
        if (element.getAttributeValue("amount") != null) {
            try {
                amount = NumUtils.parseInt(element.getAttributeValue("amount"));
            } catch (NumberFormatException e) {
            }
        }
        ItemStack itemStack;
        if (element.getText().contains(":")) itemStack = new ItemStack(Material.matchMaterial(element.getText().split(":")[0]), amount, (short) NumUtils.parseInt(element.getText().split(":")[1]));
        else itemStack = new ItemStack(Material.matchMaterial(element.getText()), amount);
        if (element.getAttributeValue("damage") != null) {
            itemStack.setDurability(Short.parseShort(element.getAttributeValue("damage")));
        }
        try {
            for (String raw : element.getAttributeValue("enchantment").split(";")) {
                String[] enchant = raw.split(":");
                try {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(StringUtils.getTechnicalName(enchant[0])), NumUtils.parseInt(enchant[1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(StringUtils.getTechnicalName(enchant[0])), 1);
                }
            }
        } catch (NullPointerException e) {

        }
        ItemMeta meta = itemStack.getItemMeta();
        if (element.getAttributeValue("name") != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('`', element.getAttributeValue("name")));
        }
        if (element.getAttributeValue("lore") != null) {
            ArrayList<String> lore = new ArrayList<>();
            for (String raw : element.getAttributeValue("lore").split("\\|")) {
                String colored = ChatColor.translateAlternateColorCodes('`', raw);
                lore.add(colored);
            }
            meta.setLore(lore);
        }
        int slot = element.getAttributeValue("slot") != null ? NumUtils.parseInt(element.getAttributeValue("slot")) : -1;
        if (element.getAttributeValue("potions") != null) {
            String potions = element.getAttributeValue("potions");
            if (potions.contains(";")) {
                for (String potion : potions.split(";")) {
                    String[] parse = potion.split(":");
                    PotionEffect effect = new PotionEffect(PotionEffectType.getByName(parse[0].toUpperCase().replaceAll(" ", "_")), NumUtils.parseInt(parse[1]), NumUtils.parseInt(parse[2]));
                    ((PotionMeta) meta).addCustomEffect(effect, true);
                }
            } else {
                String[] parse = potions.split(":");
                PotionEffect effect = new PotionEffect(PotionEffectType.getByName(parse[0].toUpperCase().replaceAll(" ", "_")), NumUtils.parseInt(parse[1]), NumUtils.parseInt(parse[2]));
                ((PotionMeta) meta).addCustomEffect(effect, true);
            }
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public static ChatColor parseChatColor(String string) {
        for (ChatColor color : ChatColor.values()) {
            if (color.name().equals(StringUtils.getTechnicalName(string))) return color;
        }
        return ChatColor.WHITE;
    }

    public static DyeColor parseDyeColor(String string) {
        for (DyeColor color : DyeColor.values()) {
            if (color.name().equals(StringUtils.getTechnicalName(string))) return color;
        }
        return DyeColor.WHITE;
    }
}
