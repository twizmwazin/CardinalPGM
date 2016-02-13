package in.twizmwaz.cardinal.util;

import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.potion.CraftPotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static ItemStack getItem(Element element) {
        int amount = Numbers.parseInt(element.getAttributeValue("amount", "1"));
        short damage = element.getAttributeValue("damage") != null ? Short.parseShort(element.getAttributeValue("damage")) : element.getText() != null && element.getText().contains(":") ? Short.parseShort(element.getText().split(":")[1]) : 0 ;
        ItemStack itemStack = null;
        if (element.getAttribute("material") != null)
            itemStack = new ItemStack(Material.matchMaterial(element.getAttributeValue("material")), amount, damage);
        if (element.getText() != "")
            itemStack = new ItemStack(Material.matchMaterial(element.getText().split(":")[0]), amount, damage);
        if (element.getAttributeValue("unbreakable") != null && Boolean.parseBoolean(element.getAttributeValue("unbreakable"))) {
            try {
                net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
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
        itemStack.setItemMeta(meta);
        String attributes = element.getAttributeValue("attributes");
        if (attributes != null) {
            //TODO: This needs to be converted to the attribute API
            itemStack = setAttributes(itemStack, attributes);
        }
        return itemStack;
    }

    private static ItemStack setAttributes(ItemStack itemStack, String attributes) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        if (nmsStack.getTag() == null) {
            nmsStack.setTag(new NBTTagCompound());
        }
        NBTTagCompound tag = nmsStack.getTag();

        NBTTagList attributeList = tag.getList("AttributeModifiers", 10);
        for (AttributeModifier modifier : parseAttributes(attributes)) {
            NBTTagCompound attributeTag = new NBTTagCompound();
            attributeTag.setString("AttributeName", modifier.getAttributeType().getName());
            attributeTag.setString("Name", modifier.getAttributeType().getName());
            attributeTag.setDouble("Amount", modifier.getValue());
            attributeTag.setInt("Operation", modifier.getOperationValue());
            attributeTag.setLong("UUIDLeast", AttributeType.modifierUUID.getLeastSignificantBits());
            attributeTag.setLong("UUIDMost", AttributeType.modifierUUID.getMostSignificantBits());
            attributeList.add(attributeTag);
        }

        tag.set("AttributeModifiers", attributeList);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }

    private static List<AttributeModifier> parseAttributes(String attributes) {
        List<AttributeModifier> modifiers = new ArrayList<>();
        for (String attribute : attributes.split(";")) {
            String[] components = attribute.split(":");
            String name = components[0];
            String operation = components[1];
            double value = Double.parseDouble(components[2]);

            AttributeType type = AttributeType.fromName(name);
            modifiers.add(new AttributeModifier(type, value, operation));
        }
        return modifiers;
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
        if (type == null) type = new CraftPotionEffectType(MobEffectList.b(potion.getText().toLowerCase().replace("_"," ")));
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
