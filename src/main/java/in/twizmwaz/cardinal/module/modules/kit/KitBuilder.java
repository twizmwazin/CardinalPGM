package in.twizmwaz.cardinal.module.modules.kit;

import in.parapengu.commons.utils.StringUtils;
import in.twizmwaz.cardinal.GameHandler;
import in.twizmwaz.cardinal.match.Match;
import in.twizmwaz.cardinal.module.BuilderData;
import in.twizmwaz.cardinal.module.ModuleBuilder;
import in.twizmwaz.cardinal.module.ModuleCollection;
import in.twizmwaz.cardinal.module.ModuleLoadTime;
import in.twizmwaz.cardinal.util.ArmorType;
import in.twizmwaz.cardinal.util.ParseUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

@BuilderData(load = ModuleLoadTime.EARLIER)
public class KitBuilder implements ModuleBuilder {

    @SuppressWarnings("unchecked")
    @Override
    public ModuleCollection load(Match match) {
        ModuleCollection results = new ModuleCollection();
        for (Element kits : match.getDocument().getRootElement().getChildren("kits")) {
            for (Element element : kits.getChildren("kit")) {
                results.add(getKit(element));
            }
        }
        results.add(new KitApplier());
        return results;
    }

    public static Kit getKit(Element element, Document document, boolean proceed) {
        if (element.getName().equalsIgnoreCase("kit") || proceed) {
            String name = null;
            if (element.getAttributeValue("name") != null) {
                name = element.getAttributeValue("name");
                for (Kit kit : GameHandler.getGameHandler().getMatch().getModules().getModules(Kit.class)) {
                    if (kit.getName().equalsIgnoreCase(name)) {
                        return kit;
                    }
                }
            }
            List<KitItem> items = new ArrayList<>(36);
            for (Element item : element.getChildren("item")) {
                ItemStack itemStack = ParseUtils.getItem(item);
                int slot = item.getAttributeValue("slot") != null ? Integer.parseInt(item.getAttributeValue("slot")) : -1;
                items.add(new KitItem(itemStack, slot));
            }
            List<KitArmor> armor = new ArrayList<>(4);
            List<Element> armors = new ArrayList<>();
            armors.addAll(element.getChildren("helmet"));
            armors.addAll(element.getChildren("chestplate"));
            armors.addAll(element.getChildren("leggings"));
            armors.addAll(element.getChildren("boots"));
            for (Element piece : armors) {
                ItemStack itemStack = new ItemStack(StringUtils.convertStringToMaterial(piece.getText()), 1);
                if (piece.getAttributeValue("damage") != null) {
                    itemStack.setDurability(Short.parseShort(piece.getAttributeValue("damage")));
                }
                if (itemStack.getItemMeta() instanceof LeatherArmorMeta && piece.getAttributeValue("color") != null) {
                    LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
                    meta.setColor(StringUtils.convertHexStringToColor(piece.getAttributeValue("color")));
                    itemStack.setItemMeta(meta);
                }
                try {
                    for (String raw : piece.getAttributeValue("enchantment").split(";")) {
                        String[] enchant = raw.split(":");
                        try {
                            itemStack.addUnsafeEnchantment(StringUtils.convertStringToEnchantment(enchant[0]), Integer.parseInt(enchant[1]));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            itemStack.addUnsafeEnchantment(StringUtils.convertStringToEnchantment(enchant[0]), 1);
                        }
                    }
                } catch (NullPointerException e) {

                }
                ArmorType type = ArmorType.getArmorType(piece.getName());
                armor.add(new KitArmor(itemStack, type));
            }
            List<PotionEffect> potions = new ArrayList<>();
            for (Element potion : element.getChildren("potion")) {
                PotionEffectType type = StringUtils.convertStringToPotionEffectType(potion.getText());
                int duration = 0;
                try {
                    duration = Integer.parseInt(potion.getAttributeValue("duration")) * 20;
                } catch (NumberFormatException e) {
                    if (potion.getAttributeValue("duration").equalsIgnoreCase("oo"))
                        duration = Integer.MAX_VALUE;
                }
                int amplifier = 0;
                if (potion.getAttributeValue("amplifier") != null) {
                    amplifier = Integer.parseInt(potion.getAttributeValue("amplifier")) - 1;
                }
                potions.add(new PotionEffect(type, duration, amplifier, true));
            }
            String parent = element.getAttributeValue("parents");
            boolean force = element.getAttributeValue("force") != null && Boolean.parseBoolean(element.getAttributeValue("force"));
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
                health = Integer.parseInt(element.getChild("health").getText()) / 2;
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
                walkSpeed = Float.parseFloat(element.getChild("walk-speed").getText()) / 5;
            } catch (NullPointerException e) {

            }
            float knockback = 0F;
            try {
                knockback = Float.parseFloat(element.getChild("knockback-reduction").getText());
            } catch (NullPointerException e) {

            }
            boolean jump = false;
            if (element.getChildren("double-jump").size() > 0)
                jump = true;
            return new Kit(name, items, armor, potions, parent, force, potionParticles, resetPearls, clear, clearItems, health, saturation, foodLevel, walkSpeed, knockback, jump);
        } else {
            return getKit(element.getParentElement(), document, true);
        }
    }

    public static Kit getKit(Element element) {
        return getKit(element, GameHandler.getGameHandler().getMatch().getDocument(), false);
    }

}
