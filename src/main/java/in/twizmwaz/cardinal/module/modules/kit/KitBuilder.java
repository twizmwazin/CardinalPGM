package in.twizmwaz.cardinal.module.modules.kit;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.Module;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.util.ArmorType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class KitBuilder implements ModuleBuilder {

    @Override
    public List<Module> load(Match match) {
        List<Module> results = new ArrayList<>();
        for (Element kits : match.getDocument().getRootElement().getChildren("kits")) {
            for (Element element : kits.getChildren("kit")) {
                String name = element.getAttributeValue("name");
                List<KitItem> items = new ArrayList<>(36);
                for (Element item : element.getChildren("item")) {
                    int amount;
                    try {
                        amount = Integer.parseInt(item.getAttributeValue("amount"));
                    } catch (NumberFormatException e) {
                        amount = 1;
                    }
                    ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(item.getText()), amount);
                    if (item.getAttributeValue("damage") != null) {
                        itemStack.setDurability(Short.parseShort(item.getAttributeValue("damage")));
                    }
                    int slot = Integer.parseInt(item.getAttributeValue("slot"));
                    items.add(new KitItem(slot, itemStack));
                }
                List<KitArmor> armor = new ArrayList<>(4);
                for (Element helmet : element.getChildren("helmet")) {
                    ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(helmet.getText()), 1);
                    if (helmet.getAttributeValue("damage") != null) {
                        itemStack.setDurability(Short.parseShort(helmet.getAttributeValue("damage")));
                    }
                    if (itemStack.getItemMeta() instanceof LeatherArmorMeta && helmet.getAttributeValue("color") != null) {
                        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                        meta.setColor(StringUtils.convertHexStringToColor(helmet.getAttributeValue("color")));
                        itemStack.setItemMeta(meta);
                    }
                    armor.add(new KitArmor(itemStack, ArmorType.HELMET));
                }
                for (Element chestplate : element.getChildren("chestplate")) {
                    ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(chestplate.getText()), 1);
                    if (chestplate.getAttributeValue("damage") != null) {
                        itemStack.setDurability(Short.parseShort(chestplate.getAttributeValue("damage")));
                    }
                    if (itemStack.getItemMeta() instanceof LeatherArmorMeta && chestplate.getAttributeValue("color") != null) {
                        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                        meta.setColor(StringUtils.convertHexStringToColor(chestplate.getAttributeValue("color")));
                        itemStack.setItemMeta(meta);
                    }
                    armor.add(new KitArmor(itemStack, ArmorType.CHESTPLATE));
                }
                for (Element leggings : element.getChildren("leggings")) {
                    ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(leggings.getText()), 1);
                    if (leggings.getAttributeValue("damage") != null) {
                        itemStack.setDurability(Short.parseShort(leggings.getAttributeValue("damage")));
                    }
                    if (itemStack.getItemMeta() instanceof LeatherArmorMeta && leggings.getAttributeValue("color") != null) {
                        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                        meta.setColor(StringUtils.convertHexStringToColor(leggings.getAttributeValue("color")));
                        itemStack.setItemMeta(meta);
                    }
                    armor.add(new KitArmor(itemStack, ArmorType.LEGGINGS));
                }
                for (Element boots : element.getChildren("boots")) {
                    ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(boots.getText()), 1);
                    if (boots.getAttributeValue("damage") != null) {
                        itemStack.setDurability(Short.parseShort(boots.getAttributeValue("damage")));
                    }
                    if (itemStack.getItemMeta() instanceof LeatherArmorMeta && boots.getAttributeValue("color") != null) {
                        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                        meta.setColor(StringUtils.convertHexStringToColor(boots.getAttributeValue("color")));
                        itemStack.setItemMeta(meta);
                    }
                    armor.add(new KitArmor(itemStack, ArmorType.BOOTS));
                }
                List<PotionEffect> potions = new ArrayList<>();
                for (Element potion : element.getChildren("potion")) {
                    PotionEffectType type = StringUtils.convertStringToPotionEffectType(potion.getText());
                    int duration = 0;
                    try {
                        duration = Integer.parseInt(potion.getAttributeValue("duration")) * 5;
                    } catch (NumberFormatException e) {
                        if (potion.getAttributeValue("duration").equalsIgnoreCase("oo")) duration = 200000;
                    }
                    int amplifier = 0;
                    if (potion.getAttributeValue("amplifier") != null) {
                        amplifier = Integer.parseInt(potion.getAttributeValue("amplifier"));
                    }
                    potions.add(new PotionEffect(type, duration, amplifier, true));
                }
                String parent = element.getAttributeValue("parents");
                boolean force = Boolean.parseBoolean(element.getAttributeValue("force"));
                boolean potionParticles;
                try {
                    potionParticles = Boolean.parseBoolean(element.getAttributeValue("potion-particles"));
                } catch (NumberFormatException e) {
                    potionParticles = true;
                }
                boolean resetPearls;
                try {
                    resetPearls = Boolean.parseBoolean(element.getAttributeValue("reset-ender-pearls"));
                } catch (NumberFormatException e) {
                    resetPearls = true;
                }
                boolean clear = false;
                if (element.getChildren("clear").size() > 0) {
                    clear = true;
                }
                boolean clearItems = false;
                if (element.getChildren("clear-items").size() > 0) {
                    clearItems = true;
                }
                int health = -1;
                try {
                    health = Integer.parseInt(element.getChild("health").getText());
                } catch (NullPointerException e) {

                }
                float saturation = 0;
                try {
                    saturation = Float.parseFloat(element.getChild("saturation").getText());
                } catch (NullPointerException e) {

                }
                int foodLevel = -1;
                try {
                    foodLevel = Integer.parseInt(element.getChild("foodlevel").getText());
                } catch (NullPointerException e) {

                }
                float walkSpeed = 0.2F;
                try {
                    walkSpeed = Float.parseFloat(element.getChild("walk-speed").getText());
                } catch (NullPointerException e) {

                }
                float knockback = 1;
                try {
                    knockback = Float.parseFloat(element.getChild("knockback-reduction").getText());
                } catch (NullPointerException e) {

                }
                boolean jump = false;
                if (element.getChildren("double-jump").size() > 0) jump = true;

                results.add(new Kit(name, items, armor, potions, parent, force, potionParticles, resetPearls, clear, clearItems, health, saturation, foodLevel, walkSpeed, knockback, jump));
            }
        }
        results.add(new KitApplier());
        return results;
    }

}
