package in.twizmwaz.cardinal.util;

import net.minecraft.server.MobEffectList;
import net.minecraft.server.NBTTagCompound;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.UUID;

public class Parser {

    public static ItemStack getItem(Element element) {
        int amount = Numbers.parseInt(element.getAttributeValue("amount", "1"));
        short damage = element.getAttributeValue("damage") != null ? Short.parseShort(element.getAttributeValue("damage")) : element.getText() != null && element.getText().contains(":") ? Short.parseShort(element.getText().split(":")[1]) : 0 ;
        ItemStack itemStack = new ItemStack(Material.AIR);
        if (element.getAttribute("material") != null) {
            itemStack = new ItemStack(Material.matchMaterial(element.getAttributeValue("material")), amount, damage);
        } else if (!element.getTextTrim().equals("")) {
            itemStack = new ItemStack(Material.matchMaterial(element.getText().split(":")[0]), amount, damage);
        }
        if (element.getAttributeValue("unbreakable") != null && Boolean.parseBoolean(element.getAttributeValue("unbreakable"))) {
            try {
                net.minecraft.server.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("Unbreakable", true);
                nmsStack.setTag(tag);
                itemStack = CraftItemStack.asBukkitCopy(nmsStack);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (element.getAttributeValue("enchantment") != null) {
            for (String raw : element.getAttributeValue("enchantment").split(";")) {
                String[] enchant = raw.split(":");
                if (enchant.length == 2) {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(Strings.getTechnicalName(enchant[0])), Numbers.parseInt(enchant[1]));
                } else if (enchant.length == 1) {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(Strings.getTechnicalName(enchant[0])), 1);
                }
            }
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
        if (element.getAttributeValue("potions") != null) {
            String potions = element.getAttributeValue("potions");
            if (potions.contains(";")) {
                for (String potion : potions.split(";")) {
                    String[] parse = potion.split(":");
                    PotionEffect effect = new PotionEffect(PotionEffectType.getByName(parse[0].toUpperCase().replaceAll(" ", "_")), Numbers.parseInt(parse[1]), Numbers.parseInt(parse[2]));
                    ((PotionMeta) meta).addCustomEffect(effect, true);
                }
            } else {
                String[] parse = potions.split(":");
                PotionEffect effect = new PotionEffect(PotionEffectType.getByName(parse[0].toUpperCase().replaceAll(" ", "_")), Numbers.parseInt(parse[1]), Numbers.parseInt(parse[2]));
                ((PotionMeta) meta).addCustomEffect(effect, true);
            }
        }
        for (Element attribute : element.getChildren("attribute")) {
            meta.addAttributeModifier(attribute.getText(), new AttributeModifier(UUID.randomUUID(), attribute.getText(), Double.parseDouble(attribute.getAttributeValue("amount", "0.0")), getOperation(attribute.getAttributeValue("operation", "add"))));
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static AttributeModifier.Operation getOperation(String operation) {
        if (NumberUtils.isNumber(operation)) {
            return AttributeModifier.Operation.fromOpcode(Integer.parseInt(operation));
        } else {
            switch (operation.toLowerCase()) {
                case("add"):
                    return AttributeModifier.Operation.ADD_NUMBER;
                case("base"):
                    return AttributeModifier.Operation.ADD_SCALAR;
                case("multiply"):
                    return AttributeModifier.Operation.MULTIPLY_SCALAR_1;
            }
        }
        return AttributeModifier.Operation.ADD_NUMBER;
    }

    public static ChatColor parseChatColor(String string) {
        for (ChatColor color : ChatColor.values()) {
            if (color.name().equals(Strings.getTechnicalName(string))) return color;
        }
        return ChatColor.WHITE;
    }

    public static DyeColor parseDyeColor(String string) {
        for (DyeColor color : DyeColor.values()) {
            if (color.name().equals(Strings.getTechnicalName(string))) return color;
        }
        return DyeColor.WHITE;
    }

    public static PotionEffect getPotion(Element potion) {
        PotionEffectType type = PotionEffectType.getByName(Strings.getTechnicalName(potion.getText()));
        if (type == null) type = new CraftPotionEffectType(MobEffectList.getByName(potion.getText().toLowerCase().replace(" ","_")));
        int duration = Numbers.parseInt(potion.getAttributeValue("duration")) == Integer.MAX_VALUE ? Numbers.parseInt(potion.getAttributeValue("duration")) : Numbers.parseInt(potion.getAttributeValue("duration")) * 20;
        int amplifier = 0;
        boolean ambient = false;

        if (potion.getAttributeValue("amplifier") != null) {
            amplifier = Numbers.parseInt(potion.getAttributeValue("amplifier")) - 1;
        }

        if (potion.getAttributeValue("ambient") != null) {
            ambient = Boolean.parseBoolean(potion.getAttributeValue("ambient").toUpperCase());
        }
        return new PotionEffect(type, duration, amplifier, ambient);
    }

    public static Pair<Material, Integer> parseMaterial(String material) {
        String type = material.split(":")[0].trim();
        Integer damageValue = material.contains(":") ? Numbers.parseInt(material.split(":")[1].trim()) : -1;
        return new ImmutablePair<>(NumberUtils.isNumber(type) ? Material.getMaterial(Integer.parseInt(type)) : Material.matchMaterial(type), damageValue);
    }

    public static String getOrderedAttribute(String attribute, Element... elements) {
        for (Element element : elements) {
            if (element.getAttributeValue(attribute) != null && !element.getAttributeValue(attribute).equals("")) {
                return element.getAttributeValue(attribute);
            }
        }
        return null;
    }

    public static String getOrderedText(Element... elements) {
        for (Element element : elements) {
            if (element.getText() != null && !element.getText().equals("")) {
                return element.getText();
            }
        }
        return null;
    }
}
