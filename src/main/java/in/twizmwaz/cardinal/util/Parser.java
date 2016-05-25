package in.twizmwaz.cardinal.util;

import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.module.modules.itemMods.ItemMods;
import in.twizmwaz.cardinal.module.modules.kit.kitTypes.KitItem;
import net.minecraft.server.CommandReplaceItem;
import net.minecraft.server.MobEffectList;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.ItemAttributeModifier;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Element;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Parser {

    public static KitItem getKitItem(Element element) {
        ItemStack itemStack = getItem(element);
        int slot = -1;
        String slotString = element.getAttributeValue("slot", "-1");
        if (NumberUtils.isNumber(slotString)) {
            slot = Integer.parseInt(slotString);
        } else {
            if (!slotString.startsWith("slot.")) slotString = "slot." + slotString;
            try {
                CommandReplaceItem replaceItem = new CommandReplaceItem();
                Method m = CommandReplaceItem.class.getDeclaredMethod("e", String.class); // Returns inventory slot (int)
                m.setAccessible(true);                                                    // from a mojang string inventory
                slot = (int)m.invoke(replaceItem, slotString);                            // like "slot.weapon.offhand"
            } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new KitItem(itemStack, slot);
    }

    public static ItemStack getItem(Element element) {
        int amount = Numbers.parseInt(element.getAttributeValue("amount", "1"));
        short damage = element.getAttributeValue("damage") != null ? Short.parseShort(element.getAttributeValue("damage")) : element.getText() != null && element.getText().contains(":") ? Short.parseShort(element.getText().split(":")[1]) : 0 ;
        ItemStack itemStack = new ItemStack(Material.AIR);
        if (element.getAttribute("material") != null) {
            itemStack = new ItemStack(Material.matchMaterial(element.getAttributeValue("material")), amount, damage);
        } else if (!element.getTextTrim().equals("")) {
            itemStack = new ItemStack(Material.matchMaterial(element.getText().split(":")[0]), amount, damage);
        }
        if (itemStack.getType() == Material.POTION) {
            itemStack = Potion.fromDamage(damage).toItemStack(amount);
        }
        if (element.getName().equalsIgnoreCase("book")) {
            itemStack = new ItemStack(Material.BOOK, amount, damage);
        }
        if (element.getAttributeValue("enchantment") != null) {
            for (String raw : element.getAttributeValue("enchantment").split(";")) {
                String[] enchant = raw.split(":");
                int lvl =  enchant.length > 1 ? Numbers.parseInt(enchant[1]) : 1;
                Enchantment enchantment = Enchantment.getByName(Strings.getTechnicalName(enchant[0]));
                if (enchantment == null) {
                    net.minecraft.server.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
                    nmsStack.addEnchantment(net.minecraft.server.Enchantment.b(enchant[0].toLowerCase().replace(" ","_")), lvl); // Enchantment.b(String) gets Enchantment by name
                    itemStack = CraftItemStack.asBukkitCopy(nmsStack);
                } else {
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(Strings.getTechnicalName(enchant[0])), lvl);
                }
            }
        }
        ItemMeta meta = itemStack.getItemMeta();
        if (element.getAttributeValue("unbreakable") != null && Boolean.parseBoolean(element.getAttributeValue("unbreakable"))) {
            meta.setUnbreakable(true);
        }
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
            for (PotionEffect effect : parseEffects(element.getAttributeValue("potions"))) {
                ((PotionMeta) meta).addCustomEffect(effect, true);
            }
        }
        if (element.getAttributeValue("attributes") != null) {
            for (ItemAttributeModifier attribute : parseAttributes((element.getAttributeValue("attributes")))) {
                meta.addAttributeModifier(attribute.getModifier().getName(), attribute);
            }
        }
        for (Element attribute : element.getChildren("attribute")) {
            meta.addAttributeModifier(attribute.getText(), getAttribute(attribute));
        }
        itemStack.setItemMeta(meta);
        if (element.getName().equalsIgnoreCase("book")) {
            BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
            bookMeta.setTitle(ChatColor.translateAlternateColorCodes('`',element.getChildText("author")));
            bookMeta.setAuthor(ChatColor.translateAlternateColorCodes('`',element.getChildText("author")));
            List<String> pages = new ArrayList<>();
            for (Element page : element.getChild("pages").getChildren("page")) {
                pages.add(ChatColor.translateAlternateColorCodes('`', page.getText()).replace("\u0009", ""));
            }
            bookMeta.setPages(pages);
            itemStack.setItemMeta(bookMeta);
        }
        if (element.getAttributeValue("color") != null) {
            LeatherArmorMeta leatherMeta = (LeatherArmorMeta) itemStack.getItemMeta();
            leatherMeta.setColor(MiscUtil.convertHexToRGB(element.getAttributeValue("color")));
            itemStack.setItemMeta(leatherMeta);
        }

        return GameHandler.getGameHandler().getMatch().getModules().getModule(ItemMods.class).applyRules(applyMeta(itemStack, element));
    }

    public static ItemStack applyMeta(ItemStack itemStack, Element element) {
        for (Element enchant : element.getChildren("enchantment")) {
            String ench = enchant.getText();
            Enchantment enchantment = Enchantment.getByName(Strings.getTechnicalName(ench));
            int lvl =  Numbers.parseInt(enchant.getAttributeValue("level"), 1);
            if (enchantment == null) {
                net.minecraft.server.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
                nmsStack.addEnchantment(net.minecraft.server.Enchantment.b(ench.toLowerCase().replace(" ","_")), lvl); // Enchantment.b(String) gets Enchantment by name
                itemStack = CraftItemStack.asBukkitCopy(nmsStack);
            } else {
                itemStack.addUnsafeEnchantment(enchantment, lvl);
            }
        }
        ItemMeta meta = itemStack.getItemMeta();
        for (Element effect : element.getChildren("effect")) {
            PotionEffect potionEffect = getPotion(effect);
            if (!((PotionMeta) meta).getCustomEffects().contains(potionEffect)) ((PotionMeta) meta).addCustomEffect(potionEffect, true);
        }
        for (Element attribute : element.getChildren("attribute")) {
            ItemAttributeModifier itemAttribute = getAttribute(attribute);
            if (!meta.getModifiedAttributes().contains(attribute.getText())) meta.addAttributeModifier(attribute.getText(), itemAttribute);
        }
        /* TODO: can-destroy & can-place-on, and all attributes
         * @link https://docs.oc.tc/modules/item_mods#itemmeta
         */
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private static List<PotionEffect> parseEffects(String effects) {
        List<PotionEffect> effectList = new ArrayList<>();
        for (String effect : effects.split(";")) {
            String[] split = effect.split(":");
            PotionEffectType type = PotionEffectType.getByName(Strings.getTechnicalName(split[0]));
            if (type == null) type = new CraftPotionEffectType(MobEffectList.getByName(split[0].toLowerCase().replace(" ","_")));
            effectList.add(new PotionEffect(type, Numbers.parseInt(split[1]), Numbers.parseInt(split[2])));
        }
        return effectList;
    }

    private static List<ItemAttributeModifier> parseAttributes(String attributes) {
        List<ItemAttributeModifier> list = new ArrayList<>();
        for (String attribute : attributes.split(";")) {
            String[] attr = attribute.split(":");
            list.add(new ItemAttributeModifier(null, new AttributeModifier(UUID.randomUUID(), attr[0], Double.parseDouble(attr[2]), getOperation(attr[1]))));
        }
        return list;
    }

    public static PotionEffect getPotion(Element potion) {
        PotionEffectType type = PotionEffectType.getByName(Strings.getTechnicalName(potion.getText()));
        if (type == null) type = new CraftPotionEffectType(MobEffectList.getByName(potion.getText().toLowerCase().replace(" ","_")));
        int duration = (int) (Strings.timeStringToExactSeconds(potion.getAttributeValue("duration")) * 20);
        int amplifier = 0;
        boolean ambient = false;
        if (potion.getAttributeValue("amplifier") != null)
            amplifier = Numbers.parseInt(potion.getAttributeValue("amplifier")) - 1;
        if (potion.getAttributeValue("ambient") != null)
            ambient = Boolean.parseBoolean(potion.getAttributeValue("ambient").toUpperCase());
        return new PotionEffect(type, duration, amplifier, ambient);
    }

    public static ItemAttributeModifier getAttribute(Element attribute) {
        return new ItemAttributeModifier(getEquipmentSlot(attribute.getAttributeValue("slot", "")),
                new AttributeModifier(UUID.randomUUID(), attribute.getText(), Double.parseDouble(attribute.getAttributeValue("amount", "0.0")), getOperation(attribute.getAttributeValue("operation", "add"))));
    }

    public static AttributeModifier.Operation getOperation(String operation) {
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

    public static EquipmentSlot getEquipmentSlot(String slotName) {
        if (!slotName.startsWith("slot.")) slotName = "slot." + slotName;
        EquipmentSlot equipmentSlot = null;
        String[] path = slotName.split("\\.");
        if (path.length != 3) return null;
        if (path[1].equalsIgnoreCase("armor")) {
            equipmentSlot = EquipmentSlot.valueOf(Strings.getTechnicalName(path[2]));
        } else if (path[1].equalsIgnoreCase("weapon")) {
            if (path[2].equalsIgnoreCase("mainhand")) equipmentSlot = EquipmentSlot.HAND;
            if (path[2].equalsIgnoreCase("offhand")) equipmentSlot = EquipmentSlot.OFF_HAND;
        }
        return equipmentSlot;
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

    public static Pair<Material, Integer> parseMaterial(String material) {
        String type = material.split(":")[0].trim();
        Integer damageValue = material.contains(":") ? Numbers.parseInt(material.split(":")[1].trim()) : -1;
        return new ImmutablePair<>(NumberUtils.isNumber(type) ? Material.getMaterial(Integer.parseInt(type)) : Material.matchMaterial(type), damageValue);
    }

    public static MaterialData parseMaterialData(String material) {
        String type = material.split(":")[0].trim();
        byte damageValue = (byte) (material.contains(":") ? Numbers.parseInt(material.split(":")[1].trim()) : -1);
        return NumberUtils.isNumber(type) ?
                new MaterialData(Material.getMaterial(Integer.parseInt(type)), damageValue):
                new MaterialData(Material.matchMaterial(type), damageValue);
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
